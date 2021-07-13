import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import client.Client
import client.Screen
import com.jakewharton.mosaic.runMosaic
import data.LocalTerminal
import data.ProvideLocalEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.HomeScreen
import ui.Join
import ui.Test

fun main() = runMosaic {
    val effects = MutableSharedFlow<suspend CoroutineScope.() -> Unit>()
    setContent {
        val navigation = remember { mutableStateOf<Screen>(Screen.Home) }
        val client = remember { Client() }
        val coroutineScope = rememberCoroutineScope()
        val terminal = LocalTerminal.current
        ProvideLocalEffect(effects) {
            when (navigation.value) {
                Screen.Home -> HomeScreen(
                    onCreateGame = {
                        coroutineScope.launch {
                            val code = client.createRoom()
                            navigation.value = Screen.Room(code)
                        }
                    },
                    onOpenJoinRoom = { navigation.value = Screen.Join },
                    onOpenTest = { navigation.value = Screen.Test },
                    onCloseLyrical = { terminal.terminal.close() }
                )
                Screen.Join -> Join(
                    onJoinRoom = { navigation.value = Screen.Room(it) },
                    onBack = { navigation.value = Screen.Home }
                )
                is Screen.Room -> TODO()
                is Screen.Test -> Test()
            }
            //Test()
        }
    }
    withContext(IO) {
        effects.collect { function ->
            function.invoke(this)
        }
    }
}