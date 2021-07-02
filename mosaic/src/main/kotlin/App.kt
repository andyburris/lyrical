import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import client.ClientRoomMachine
import client.Screen
import com.jakewharton.mosaic.runMosaic
import data.LocalTerminal
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.jline.terminal.TerminalBuilder

fun main() = runMosaic {
    val navigation = remember { mutableStateOf<Screen>(Screen.Home) }
    setContent {
        when(navigation.value) {
            Screen.Home -> HomeScreen(
                onCreateGame = {},
                onOpenJoinRoom = { navigation.value = Screen.Join },
                onCloseLyrical = {  }
            )
            Screen.Join -> Join(
                onJoinRoom = {},
                onBack = { navigation.value = Screen.Home }
            )
            is Screen.Room -> TODO()
        }
    }
}