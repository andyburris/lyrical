package room

import User
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import client.Client
import client.ClientRoomMachine
import org.jetbrains.compose.common.ui.Modifier
import room.game.GameRouter
import room.lobby.LobbyScreen
import server.RoomCode
import server.RoomState

@Composable
fun RoomRouter(user: User, code: RoomCode, client: Client, modifier: Modifier = Modifier) {
    val roomMachine = remember { ClientRoomMachine(code, client) }
    val room = roomMachine.roomFlow.collectAsState(null).value
    when(val state = room?.state) {
        null -> LoadingScreen("Loading into room", modifier)
        is RoomState.Lobby -> LobbyScreen(
            code = room.code,
            isHost = user == room.host,
            state = state,
            modifier = modifier,
            onUserAction = { roomMachine.handleAction(it) }
        )
        is RoomState.Loading -> LoadingScreen("Loading Game", modifier)
        is RoomState.Game.Client -> GameRouter(state, modifier) { roomMachine.handleAction(it) }
    }
}