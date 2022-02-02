import androidx.compose.runtime.*
import client.*
import home.HomeScreen
import join.JoinScreen
import kotlinx.coroutines.launch
import platform.LyricalTheme
import room.RoomRouter

@Composable
fun App() {
    val navigation = remember { mutableStateOf<Screen>(Screen.Home) }
    val userMachine = remember { UserMachine() }
    val httpClient = remember { defaultHttpClient(userMachine) }
    val spotifyRepository by remember { spotifyRepository(RemoteSpotifyRepository(httpClient)) }.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val client = Client(userMachine, httpClient, spotifyRepository)
    LyricalTheme {
        when (val screen = navigation.value) {
            Screen.Home -> HomeScreen(
                onCreateGame = {
                    coroutineScope.launch {
                        val code = client.createRoom()
                        println("code = $code")
                        navigation.value = Screen.Room(code)
                    }
                },
                onJoinRoom = { navigation.value = Screen.Join }
            )
            Screen.Join -> JoinScreen(
                onBackHome = { navigation.value = Screen.Home },
                onJoinRoom = { navigation.value = Screen.Room(it) }
            )
            is Screen.Room -> RoomRouter(
                code = screen.roomCode,
                client = client,
                onNavigateBack = { navigation.value = Screen.Home }
            )
            is Screen.Test -> TODO()
        }
    }
}