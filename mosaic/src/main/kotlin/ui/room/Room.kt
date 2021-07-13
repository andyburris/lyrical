package ui.room

import User
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import client.Client
import client.ClientRoomMachine
import server.RoomCode
import server.RoomState
import ui.room.game.GameRouter


@Composable
fun RoomRouter(user: User, code: RoomCode, client: Client) {
    val roomMachine = remember { ClientRoomMachine(code, client) }
    val room = roomMachine.roomFlow.collectAsState(null).value
    when(val state = room?.state) {
        null -> Loading(
            loadingDescription = "Loading into room",
            onLeaveRoom = {  }
        )
        is RoomState.Lobby -> Lobby(
            roomCode = room.code,
            user = user,
            host = room.host,
            state = state,
            onUserAction = { roomMachine.handleAction(it) }
        )
        is RoomState.Loading -> Loading(
            loadingDescription = "Loading Game",
            onLeaveRoom = {  }
        )
        is RoomState.Game.Client -> GameRouter(state) { roomMachine.handleAction(it) }
    }
}