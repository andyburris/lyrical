import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import client.Screen
import platform.LyricalTheme
import home.HomeScreen

@Composable
fun App() {
    val navigation = remember { mutableStateOf<Screen>(Screen.Home) }
    LyricalTheme {
        when(navigation.value) {
            Screen.Home -> HomeScreen(
                onCreateGame = { },
                onJoinRoom = { }
            )
            Screen.Join -> TODO()
            is Screen.Room -> TODO()
        }
    }
}