package server

import GameAnswer
import GameConfig
import GameScreen
import User
import answeredQuestions
import client.ClientGameQuestion
import client.Leaderboard
import client.LeaderboardPlayer
import kotlinx.serialization.Serializable
import model.GenericPlaylist
import replace
import totalPoints
import withAnswer

sealed interface BaseRoomState {
    val config: GameConfig
}
sealed interface ServerRoomState : BaseRoomState
sealed interface ClientRoomState : BaseRoomState

@Serializable
sealed class RoomState {
    abstract val config: GameConfig
    @Serializable
    data class Lobby(
        val playlists: List<GenericPlaylist> = emptyList(),
        override val config: GameConfig = GameConfig(),
        val joinedUsers: List<User> = emptyList()
    ) : ServerRoomState, ClientRoomState

    @Serializable
    data class Loading(val users: List<User>, override val config: GameConfig) : ServerRoomState, ClientRoomState

    @Serializable sealed class Game {
        abstract val config: GameConfig
        abstract val gameScreen: GameScreen
        @Serializable
        data class Server(
            override val config: GameConfig,
            override val gameScreen: GameScreen,
            val questions: List<ServerGameQuestion>,
            val userStates: Map<User, UserState>
        ) : ServerRoomState, Game()

        @Serializable
        data class Client(
            override val config: GameConfig,
            override val gameScreen: GameScreen,
            val questions: List<ClientGameQuestion>,
            val leaderboard: Leaderboard
        ) : ClientRoomState, Game()
    }

}

val RoomState.Lobby.isValid get() = this.playlists.sumOf { it.trackCount }.let { it >= 10 && it >= config.amountOfSongs }

fun RoomState.Game.withScreen(gameScreen: GameScreen) = when(this) {
    is RoomState.Game.Server -> this.copy(gameScreen = gameScreen)
    is RoomState.Game.Client -> this.copy(gameScreen = gameScreen)
}
fun RoomState.Game.hasNextQuestionScreen() = (this.gameScreen as? GameScreen.Answer)?.let { it.questionIndex < this.config.amountOfSongs - 1 } ?: false
private fun RoomState.Game.withNextQuestionScreenGeneric() = this.withScreen(gameScreen = GameScreen.Question((this.gameScreen as GameScreen.Answer).questionIndex + 1))
private fun RoomState.Game.withNextAnswerScreenGeneric() = this.withScreen(gameScreen = GameScreen.Answer((this.gameScreen as GameScreen.Question).questionIndex))
private fun RoomState.Game.withEndScreenGeneric() = this.withScreen(gameScreen = GameScreen.Summary)

fun RoomState.Game.Server.withAnswer(user: User, questionNumber: Int, answer: GameAnswer) = this.copy(userStates = userStates + (user to userStates.getValue(user).withAnswer(questionNumber, answer)))
fun RoomState.Game.Server.withConnection(user: User, joined: Boolean) = this.copy(userStates = userStates + (user to userStates.getValue(user).withConnection(joined)))
fun RoomState.Game.Server.withNextAnswerScreen() = this.withNextAnswerScreenGeneric() as RoomState.Game.Server
fun RoomState.Game.Server.withNextQuestionScreen() = this.withNextQuestionScreenGeneric() as RoomState.Game.Server
fun RoomState.Game.Server.withEndScreen() = this.withEndScreenGeneric() as RoomState.Game.Server

fun ServerRoomState.toClientRoomState(user: User): ClientRoomState = when(this) {
    is RoomState.Lobby -> this
    is RoomState.Loading -> this
    is RoomState.Game.Server -> this.toClientRoomGameState(user)
}

fun RoomState.Game.Server.toClientRoomGameState(user: User) = RoomState.Game.Client(
    config = config,
    gameScreen = gameScreen,
    questions = this.questions.toClientQuestions(currentGameScreen = this.gameScreen, userAnswers = this.userStates.getValue(user).answers, this.config.showSourcePlaylist),
    leaderboard = this.userStates.toLeaderboard(),
)
fun RoomState.Game.Client.withQuestion(questionIndex: Int, question: ClientGameQuestion) = this.copy(questions = this.questions.replace(questionIndex) { question })
fun RoomState.Game.Client.withLeaderboardPlayers(players: List<LeaderboardPlayer>) = this.copy(leaderboard = Leaderboard(players))
fun RoomState.Game.Client.withNextAnswerScreen() = this.withNextAnswerScreenGeneric()  as RoomState.Game.Client
fun RoomState.Game.Client.withNextQuestionScreen() = this.withNextQuestionScreenGeneric()  as RoomState.Game.Client
fun RoomState.Game.Client.withEndScreen() = this.withEndScreenGeneric()  as RoomState.Game.Client


@Serializable
data class UserState(val connected: Boolean, val answers: List<GameAnswer>)
fun UserState.withAnswer(questionNumber: Int, answer: GameAnswer) = this.copy(answers = answers.withAnswer(questionNumber, answer))
fun UserState.withConnection(connected: Boolean) = this.copy(connected = connected)

fun Map<User, UserState>.toLeaderboard() = Leaderboard(
    players = this.map { (user, state) -> LeaderboardPlayer(user, state.answers.totalPoints(), state.answers.answeredQuestions(), state.connected) }.sortedBy { it.points }
)