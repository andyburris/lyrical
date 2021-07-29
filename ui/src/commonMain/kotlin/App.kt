import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import client.Client
import client.Screen
import client.UserMachine
import client.decodeUser
import home.HomeScreen
import join.JoinScreen
import kotlinx.coroutines.launch
import platform.LyricalTheme
import room.RoomRouter

@Composable
fun App() {
    val navigation = remember { mutableStateOf<Screen>(Screen.Home) }
    //val spotifyRepository = remember { SpotifyLogin() }
    val userMachine = remember { UserMachine() }
    val client = remember { Client(userMachine) }
    val coroutineScope = rememberCoroutineScope()
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
        }
    }
}