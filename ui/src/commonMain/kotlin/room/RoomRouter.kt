package room

import User
import androidx.compose.runtime.*
import client.Client
import client.ClientRoomMachine
import client.decodeUser
import kotlinx.coroutines.launch
import org.jetbrains.compose.common.ui.Modifier
import room.game.GameRouter
import room.lobby.LobbyScreen
import server.RoomCode
import server.RoomState

@Composable
fun RoomRouter(code: RoomCode, client: Client, modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {
    val user = client.userMachine.currentUser.collectAsState(initial = null).value
    when(user) {
        null -> NotLoggedInRoomRouter(modifier)
        else -> LoggedInRoomRouter(code, user, client, modifier, onNavigateBack)
    }
}

@Composable
fun NotLoggedInRoomRouter(modifier: Modifier = Modifier) {
    LoadingScreen("Loading into room", modifier)
}

@Composable
fun LoggedInRoomRouter(code: RoomCode, user: User, client: Client, modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val roomMachine = remember { ClientRoomMachine(code, user, client, coroutineScope) }
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
            onUserAction = { coroutineScope.launch { roomMachine.handleAction(it) } },
            onNavigateBack = onNavigateBack
        )
        is RoomState.Loading -> LoadingScreen("Loading Game", modifier)
        is RoomState.Game.Client -> GameRouter(
            game = state,
            modifier = modifier,
            onGameAction = { coroutineScope.launch { roomMachine.handleAction(it) } },
            onNavigateBack = {
                coroutineScope.launch { roomMachine.handleAction(UserAction.LeaveRoom) }
                onNavigateBack.invoke()
            }
        )
    }
    DisposableEffect(roomMachine) {
        onDispose { coroutineScope.launch { roomMachine.handleAction(UserAction.LeaveRoom) } }
    }
}