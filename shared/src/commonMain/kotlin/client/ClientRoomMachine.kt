package client

import Room
import User
import UserAction
import joinedUsers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import points
import server.*
import withGame
import withLobby

class ClientRoomMachine(
    val code: RoomCode,
    val user: User,
    val client: Client,
    val coroutineScope: CoroutineScope,
) {
    private val actionFlow: MutableSharedFlow<UserAction> = MutableSharedFlow()
    val roomFlow: SharedFlow<Room.Client> = client.subscribeToRoom(code, actionFlow).runningRoomFold(user).shareIn(coroutineScope, SharingStarted.Eagerly)
    suspend fun handleAction(action: UserAction) = actionFlow.emit(action)
}

fun Flow<ClientResponse>.runningRoomFold(currentUser: User): Flow<Room.Client> {
    var room: Room.Client? = null
    return transform { response ->
        println("runningRoomFold transform, response = $response")
        when(response) {
            is ClientResponse.Error -> TODO()
            is ClientResponse.Joined -> room = response.currentRoom
            ClientResponse.Left -> TODO()
            ClientResponse.Kicked -> TODO()
            is ClientResponse.Room -> room = room?.applyResponse(response, currentUser)
        }
        println("applied transform, room = $room")
        emit(room)
    }.filterNotNull()
}

fun Room.Client.applyResponse(response: ClientResponse.Room, currentUser: User): Room.Client = when(response) {
    is ClientResponse.Room.UsersUpdated -> this.copy(state = this.state.withUsers(response.users))
    is ClientResponse.Room.Lobby -> this.withLobby { it.applyLobbyResponse(response) }
    ClientResponse.Room.Loading -> this.copy(state = RoomState.Loading(this.joinedUsers, this.state.config))
    is ClientResponse.Room.Game -> this.withGame { it.applyGameResponse(response, currentUser) }
}

private fun ClientRoomState.withUsers(users: List<User>) = when(this) {
    is RoomState.Lobby -> this.copy(joinedUsers = users)
    is RoomState.Loading -> this.copy(users = users)
    is RoomState.Game.Client -> {
        val newLeaderboardPlayers = this.leaderboard.players.map { if (it.user in users) it.copy(connected = true) else it.copy(connected = false) }
        this.withLeaderboardPlayers(newLeaderboardPlayers)
    }
}

private fun RoomState.Lobby.applyLobbyResponse(response: ClientResponse.Room.Lobby): RoomState.Lobby = when(response) {
    is ClientResponse.Room.Lobby.PlaylistsUpdated -> this.copy(playlists = response.playlists)
    is ClientResponse.Room.Lobby.ConfigUpdated -> this.copy(config = response.config)
}

private fun RoomState.Game.Client.applyGameResponse(response: ClientResponse.Room.Game, currentUser: User): RoomState.Game.Client = when(response) {
    is ClientResponse.Room.Game.SendQuestion -> {
        val question = ClientGameQuestion.Unanswered(response.lyric, Hints.None, response.sourcePlaylist)
        this.withQuestion(response.questionNumber, question).withNextQuestionScreen()
    }
    is ClientResponse.Room.Game.SendHint.NextLyric -> {
        val question = (this.questions[response.questionNumber] as ClientGameQuestion.Unanswered).withNextLyric(response.lyric)
        this.withQuestion(response.questionNumber, question)
    }
    is ClientResponse.Room.Game.SendHint.Artist -> {
        val question = (this.questions[response.questionNumber] as ClientGameQuestion.Unanswered).withArtist(response.artist)
        this.withQuestion(response.questionNumber, question)
    }
    is ClientResponse.Room.Game.SendAnswer -> {
        val newLeaderboardPlayers = this.leaderboard.players.map { if (it.user == currentUser) it.copy(points = it.points + response.answer.answer.points, questionsAnswered = it.questionsAnswered + 1) else it }
        this.withQuestion(response.questionNumber, response.answer).withNextAnswerScreen().withLeaderboardPlayers(newLeaderboardPlayers)
    }
    is ClientResponse.Room.Game.UpdateLeaderboard -> {
        val newLeaderboardPlayers = this.leaderboard.players.map { if (it.user == response.player.user) response.player else it }
        this.withLeaderboardPlayers(newLeaderboardPlayers)
    }
    is ClientResponse.Room.Game.Ended -> this.withEndScreen()
}