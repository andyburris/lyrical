package room

import User
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import client.Client
import client.ClientRoomMachine
import kotlinx.coroutines.launch
import org.jetbrains.compose.common.ui.Modifier
import room.game.GameRouter
import room.lobby.LobbyScreen
import server.RoomCode
import server.RoomState

@Composable
fun RoomRouter(user: User, code: RoomCode, client: Client, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val roomMachine = remember { ClientRoomMachine(code, client, coroutineScope) }
    val room = roomMachine.roomFlow.collectAsState(null).value
    println("user = $user, roomHost = ${room?.host}, isHost = ${user == room?.host}")
    when(val state = room?.state) {
        null -> LoadingScreen("Loading into room", modifier)
        is RoomState.Lobby -> LobbyScreen(
            code = room.code,
            isHost = user == room.host,
            state = state,
            spotifyRepository = client.spotifyRepository,
            modifier = modifier,
            onUserAction = { coroutineScope.launch { roomMachine.handleAction(it) } }
        )
        is RoomState.Loading -> LoadingScreen("Loading Game", modifier)
        is RoomState.Game.Client -> GameRouter(state, modifier) { coroutineScope.launch { roomMachine.handleAction(it) } }
    }
}