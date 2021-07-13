package common

import androidx.compose.runtime.*
import com.jakewharton.mosaic.Row
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.TextStyle
import data.LocalEffect
import data.LocalTerminal
import data.TerminalAction
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(InternalCoroutinesApi::class)
@Composable
fun Actions(vararg actions: TerminalAction) {
    Row {
        Text("Actions: ", style = TextStyle.Dim)
        val actionsText = actions.filter { it.isShown }.joinToString { "${it.name} (${it.keycodes.joinToString()})" }
        Text(actionsText)
    }
    val terminal = LocalTerminal.current
    val compositionScope = rememberCoroutineScope()
    LocalEffect(compositionScope) {
        actions.forEach { action ->
            action.keycodes.forEach { keycode ->
                terminal.onKeycodePress(keycode).onEach { keycode ->
                    action.onAction.invoke(keycode)
                }.launchIn(compositionScope)
            }
        }
    }
}