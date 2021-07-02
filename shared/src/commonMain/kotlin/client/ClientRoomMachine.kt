package client

import Room
import User
import UserAction
import joinedUsers
import kotlinx.coroutines.flow.*
import server.*
import withGame
import withLobby

class ClientRoomMachine(val code: RoomCode, val client: Client) {
    private val actionFlow: MutableSharedFlow<UserAction> = MutableSharedFlow()
    val roomFlow: Flow<Room.Client> = client.subscribeToRoom(code, actionFlow).runningRoomFold()
    fun handleAction(action: UserAction) = actionFlow.tryEmit(action)
}

fun Flow<ClientResponse>.runningRoomFold(): Flow<Room.Client> {
    var room: Room.Client? = null
    return transform { response ->
        when(response) {
            is ClientResponse.Error -> TODO()
            is ClientResponse.Joined -> room = response.currentRoom
            ClientResponse.Left -> TODO()
            ClientResponse.Kicked -> TODO()
            is ClientResponse.Room -> room = room?.applyResponse(response)
        }
        emit(room)
    }.filterNotNull()
}

fun Room.Client.applyResponse(response: ClientResponse.Room): Room.Client = when(response) {
    is ClientResponse.Room.UsersUpdated -> this.copy(state = this.state.withUsers(response.users))
    is ClientResponse.Room.Lobby -> this.withLobby { it.applyLobbyResponse(response) }
    ClientResponse.Room.Loading -> this.copy(state = RoomState.Loading(this.joinedUsers))
    is ClientResponse.Room.Game -> this.withGame { it.applyGameResponse(response) }
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
    is ClientResponse.Room.Lobby.PlaylistsUpdated -> this.copy(playlists = playlists)
    is ClientResponse.Room.Lobby.ConfigUpdated -> this.copy(config = config)
}

private fun RoomState.Game.Client.applyGameResponse(response: ClientResponse.Room.Game): RoomState.Game.Client = when(response) {
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
    is ClientResponse.Room.Game.SendAnswer -> this.withQuestion(response.questionNumber, response.answer).withNextAnswerScreen()
    is ClientResponse.Room.Game.UpdateLeaderboard -> {
        val newLeaderboardPlayers = this.leaderboard.players.map { if (it.user == response.player.user) response.player else it }
        this.withLeaderboardPlayers(newLeaderboardPlayers)
    }
    is ClientResponse.Room.Game.Ended -> this.withEndScreen()
}