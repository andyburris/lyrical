package server

import Action
import GameAction
import GameConfig
import LobbyAction
import LyricsRepository
import Room
import UserAction
import SpotifyRepository
import User
import ActionWithUser
import GameAnswer
import GameScreen
import joinedUsers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable
import model.getRandomSongs
import randomLyricIndex
import withGame
import withLobby
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@OptIn(ExperimentalTime::class)
class RoomMachine(
    val code: RoomCode,
    val host: User,
    val spotifyRepository: SpotifyRepository,
    val lyricsRepository: LyricsRepository,
    val coroutineScope: CoroutineScope,
) {
    var room = Room.Server(code, host, RoomState.Lobby())
    private val rawActionFlow = MutableSharedFlow<ActionWithUser>()
    val actionResultFlow: SharedFlow<ActionResult> = rawActionFlow.applyActions().shareIn(coroutineScope, SharingStarted.Eagerly)

    suspend fun handleAction(action: UserAction, user: User) {
        println("handling action = $action for user = $user")
        rawActionFlow.emit(ActionWithUser(action, user))
    }

    private fun Flow<ActionWithUser>.applyActions(): Flow<ActionResult> = transform { (action, user) ->
        println("applyActions transforming action = $action for user = $user")
        suspend fun onLoadGame(lobby: RoomState.Lobby) {
            val usersInLoading = (room.state as RoomState.Loading).users
            val randomTracks = lobby.playlists.getRandomSongs(spotifyRepository, lobby.config)
            val withLyrics = lyricsRepository.getLyricsFor(randomTracks)
            val questions = withLyrics.map { gameTrack -> ServerGameQuestion(gameTrack, gameTrack.lyrics.randomLyricIndex(lobby.config.difficulty, gameTrack.track.name)) }
            val userStates = lobby.joinedUsers.associateWith { user -> UserState(connected = user in usersInLoading, answers = (0 until lobby.config.amountOfSongs).map { GameAnswer.Unanswered(emptyList()) }) }
            val startGameAction = ServerAction.StartGame(questions, lobby.config, userStates)
            val roomResult = room.applyAction(startGameAction, user)
            room = when(roomResult) {
                is RoomResult.Applied -> roomResult.room
                is RoomResult.SideEffect -> roomResult.applied.room
                is RoomResult.Error -> room
            }
            emit(roomResult.toActionResult(user, startGameAction))
        }
        suspend fun onStartTimer(time: Duration) {
            delay(time)
            val roomState = room.state as RoomState.Game.Server
            val gameState = roomState.gameScreen as GameScreen.Question
            val unansweredUsers = roomState.userStates.filter { it.value.answers[gameState.questionIndex] is GameAnswer.Unanswered }
            val outOfTimeAction = ServerAction.RanOutOfTime(unansweredUsers)
            val roomResult = room.applyAction(outOfTimeAction, user)
            room = when(roomResult) {
                is RoomResult.Applied -> roomResult.room
                is RoomResult.SideEffect -> roomResult.applied.room
                is RoomResult.Error -> room
            }
            emit(roomResult.toActionResult(user, outOfTimeAction))
        }

        val roomResult = room.applyAction(action, user)
        println("new roomResult = $roomResult")
        room = when(roomResult) {
            is RoomResult.Applied -> roomResult.room
            is RoomResult.SideEffect -> roomResult.applied.room
            is RoomResult.Error -> room
        }
        emit(roomResult.toActionResult(user, action))
        if (roomResult is RoomResult.SideEffect) {
/*            coroutineScope { this }.launch {
                when(roomResult) {
                    is RoomResult.SideEffect.LoadGame -> onLoadGame(roomResult.lobby)
                    is RoomResult.SideEffect.StartTimer -> onStartTimer(roomResult.time)
                }
            }*/
            //TODO: run side effect in coroutine
            when(roomResult) {
                is RoomResult.SideEffect.LoadGame -> onLoadGame(roomResult.lobby)
                is RoomResult.SideEffect.StartTimer -> onStartTimer(roomResult.time)
            }
/*            coroutineScope.launch {
                when(roomResult) {
                    is RoomResult.SideEffect.LoadGame -> onLoadGame(roomResult.lobby)
                    is RoomResult.SideEffect.StartTimer -> onStartTimer(roomResult.time)
                }
            }*/
        }
    }

}


@Serializable
sealed class ServerError {
    @Serializable object WrongQuestion : ServerError()
    @Serializable object AlreadyAnswered : ServerError()
    @Serializable object NotInLobby : ServerError()
    @Serializable object NotInGame : ServerError()
    @Serializable object NotAHost : ServerError()
    @Serializable object UserAlreadyLeft : ServerError()
    @Serializable object KickedYourself : ServerError()
    @Serializable object InvalidConfig : ServerError()
    @Serializable object NotEnoughPlaylists : ServerError()
    @Serializable object AlreadyInGame : ServerError()
    @Serializable object AlreadyReceivedHint : ServerError()
    @Serializable object WrongScreen : ServerError()
}

sealed class ActionResult {
    data class Applied(val room: Room.Server, val action: Action, val actionUser: User) : ActionResult()
    data class Error(val error: ServerError, val actionUser: User) : ActionResult()
}

@OptIn(ExperimentalTime::class)
sealed class RoomResult {
    data class Applied(val room: Room.Server) : RoomResult()
    sealed class SideEffect : RoomResult() {
        abstract val applied: RoomResult.Applied
        data class LoadGame(val lobby: RoomState.Lobby, override val applied: RoomResult.Applied) : SideEffect()
        data class StartTimer (val time: Duration, override val applied: Applied) : SideEffect()
    }
    data class Error(val error: ServerError) : RoomResult()
    fun toActionResult(user: User, action: Action): ActionResult = when(this) {
        is Applied -> ActionResult.Applied(room, action, user)
        is SideEffect -> this.applied.toActionResult(user, action)
        is Error -> ActionResult.Error(error, user)
    }
}

suspend fun Room.Server.applyAction(action: Action, user: User): RoomResult {
    when(action) {
        is GameAction -> return when (this.state) {
            is RoomState.Game.Server -> this.state.applyAction(action, user, host).toRoomResult(this)
            !is RoomState.Game.Server -> RoomResult.Error(ServerError.NotInGame)
        }
        is LobbyAction -> return when (this.state) {
            is RoomState.Lobby -> this.state.applyAction(action, user, host).toRoomResult(this)
            !is RoomState.Lobby -> RoomResult.Error(ServerError.NotInLobby)
        }
        UserAction.Host.LoadGame -> when (user) {
            this.host -> {
                val lobby = when(this.state) {
                    is RoomState.Lobby -> this.state
                    !is RoomState.Lobby -> return RoomResult.Error(ServerError.NotInLobby)
                }
                println("loading game, current playlists = ${lobby.playlists}")
                if (!lobby.isValid) return RoomResult.Error(ServerError.NotEnoughPlaylists)
                val applied = RoomResult.Applied(this.copy(state = RoomState.Loading(lobby.joinedUsers, lobby.config)))
                return RoomResult.SideEffect.LoadGame(lobby, applied)
            }
            else -> return RoomResult.Error(ServerError.NotAHost)
        }

        is UserAction.JoinRoom -> return when {
            this.state is RoomState.Lobby -> RoomResult.Applied(this.withLobby { it.copy(joinedUsers = it.joinedUsers + user) })
            this.state is RoomState.Game.Server && user in this.state.userStates -> RoomResult.Applied(this.withGame { it.withConnection(user, true) })
            else -> RoomResult.Error(ServerError.AlreadyInGame)
        }
        UserAction.LeaveRoom -> return when(this.state) {
            is RoomState.Lobby -> RoomResult.Applied(this.withLobby { it.copy(joinedUsers = it.joinedUsers - user) })
            is RoomState.Loading -> RoomResult.Applied(this.copy(state = this.state.copy(users = this.state.users - user)))
            is RoomState.Game.Server -> RoomResult.Applied(this.withGame { it.withConnection(user, false) })
        }.let { if (user == this.host) it.copy(room = it.room.copy(host = this.joinedUsers.first())) else it }
        is ServerAction.StartGame -> {
            val gameState = RoomState.Game.Server(action.config, GameScreen.Question(0), action.questions, action.userStates)
            return RoomResult.Applied(this.copy(state = gameState))
        }
        is ServerAction.RanOutOfTime -> {
            val gameState = ((this.state as RoomState.Game.Server).gameScreen as GameScreen.Question)
            val questionIndex = gameState.questionIndex
            val newRoom = this.withGame { game ->
                action.unansweredUsers.toList().fold(game) { game, (user, userState) ->
                    val answer = GameAnswer.Answered.Missed(userState.answers[questionIndex].hintsUsed)
                    game.withAnswer(user, questionIndex, answer)
                }
            }
            return RoomResult.Applied(newRoom)
        }
    }
}

@OptIn(ExperimentalTime::class)
sealed class GameResult {
    data class Applied(val game: RoomState.Game.Server) : GameResult()
    sealed class SideEffect : GameResult() {
        abstract val applied: GameResult.Applied
        data class StartTimer (val time: Duration, override val applied: Applied) : SideEffect()
    }
    data class Error(val error: ServerError) : GameResult()
    fun toRoomResult(room: Room.Server): RoomResult = when(this) {
        is Applied -> RoomResult.Applied(room.withGame { game })
        is SideEffect.StartTimer -> RoomResult.SideEffect.StartTimer(time, applied.toRoomResult(room) as RoomResult.Applied)
        is Error -> RoomResult.Error(error)
    }
}

@OptIn(ExperimentalTime::class)
fun RoomState.Game.Server.applyAction(action: GameAction, user: User, host: User) : GameResult {
    when(action) {
        is GameAction.Question -> {
            if(this.gameScreen !is GameScreen.Question) return GameResult.Error(ServerError.WrongScreen)
            if (this.gameScreen.questionIndex != action.questionIndex) return GameResult.Error(ServerError.WrongQuestion)
            return when(action) {
                is GameAction.Question.AnswerQuestion -> when(val serverUserAnswer = this.userStates.getValue(host).answers[action.questionIndex]) {
                    !is GameAnswer.Unanswered -> GameResult.Error(ServerError.AlreadyAnswered)
                    else -> {
                        val answered: GameAnswer.Answered = this.questions[action.questionIndex].check(action.answer, serverUserAnswer)
                        val gameState: RoomState.Game.Server = this.withAnswer(host, action.questionIndex, answered)
                        val isLastAnswer = gameState.userStates.all { it.value.answers[action.questionIndex] is GameAnswer.Answered }
                        val applied = GameResult.Applied(gameState.let { if (isLastAnswer) it.withNextAnswerScreen() else it })
                        println("applied GameAction.Question.AnswerQuestion, isLastAnswer = $isLastAnswer, answered = $answered, gameState = $gameState, applied = $applied")
                        applied
                    }
                }
                is GameAction.Question.RequestHint -> {
                    val usedHints = this.userStates.getValue(host).answers[action.questionIndex].hintsUsed
                    when (action.hint) {
                        in usedHints -> GameResult.Error(ServerError.AlreadyReceivedHint)
                        else -> GameResult.Applied(this.withAnswer(host, action.questionIndex, GameAnswer.Unanswered(usedHints + action.hint)))
                    }
                }
            }
        }
        is GameAction.Answer -> {
            if (this.gameScreen !is GameScreen.Answer) return GameResult.Error(ServerError.WrongScreen)
            return when(action) {
                is GameAction.Answer.NextScreen -> when {
                    user != host -> GameResult.Error(ServerError.NotAHost)
                    this.hasNextQuestionScreen() -> GameResult.Applied(this.withNextQuestionScreen())
                    else -> GameResult.Applied(this.withEndScreen())
                }.let {
                    if (config.timer != 0 && it is GameResult.Applied) GameResult.SideEffect.StartTimer(config.timer.seconds, it) else it
                }
            }
        }
    }
}

sealed class LobbyResult {
    data class Applied(val lobby: RoomState.Lobby) : LobbyResult()
    data class Error(val error: ServerError) : LobbyResult()
    fun toRoomResult(room: Room.Server) = when(this) {
        is Applied -> RoomResult.Applied(room.withLobby { lobby })
        is Error -> RoomResult.Error(error)
    }
}

fun RoomState.Lobby.applyAction(action: LobbyAction, user: User, host: User): LobbyResult {
    when(action) {
        is LobbyAction.Host -> {
            if(user != host) return LobbyResult.Error(ServerError.NotAHost)
            when(action) {
                is LobbyAction.Host.UpdatePlaylists -> return LobbyResult.Applied(this.copy(playlists = action.playlists))
                is LobbyAction.Host.KickUser -> return when (action.kicking) {
                    user -> LobbyResult.Error(ServerError.KickedYourself)
                    in this.joinedUsers -> LobbyResult.Applied(this.copy(joinedUsers = this.joinedUsers - action.kicking))
                    else -> LobbyResult.Error(ServerError.UserAlreadyLeft)
                }
                is LobbyAction.Host.UpdateConfig -> return when {
                    action.updatedConfig.isValid(this) -> LobbyResult.Applied(this.copy(config = action.updatedConfig))
                    else -> return LobbyResult.Error(ServerError.InvalidConfig)
                }
            }
        }
    }
}

fun GameConfig.isValid(lobby: RoomState.Lobby): Boolean {
    val validSongs = this.amountOfSongs in 1..100 && this.amountOfSongs <= lobby.playlists.sumOf { it.trackCount }
    return validSongs
}