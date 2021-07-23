import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import client.Client
import client.Screen
import client.decodeUser
import home.HomeScreen
import kotlinx.coroutines.launch
import platform.LyricalTheme
import room.RoomRouter

@Composable
fun App() {
    val navigation = remember { mutableStateOf<Screen>(Screen.Home) }
    val client = remember { Client() }
    val coroutineScope = rememberCoroutineScope()
    LyricalTheme {
        when(val screen = navigation.value) {
            Screen.Home -> HomeScreen(
                onCreateGame = {
                    println("Clicked create game")
                    coroutineScope.launch {
                        println("before create room")
                        val code = client.createRoom()
                        println("code = $code")
                        navigation.value = Screen.Room(code)
                    }
               },
                onJoinRoom = { println("clicked join room") }
            )
            Screen.Join -> {

            }
            is Screen.Room -> {
                RoomRouter(
                    user = client.tokenStorage.getSavedTokens()!!.decodeUser(),
                    code = screen.roomCode,
                    client = client,
                )
            }
        }
    }
}