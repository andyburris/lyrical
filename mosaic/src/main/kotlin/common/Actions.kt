package common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.jakewharton.mosaic.Row
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.TextStyle
import data.LocalTerminal
import data.TerminalAction

@Composable
fun Actions(vararg actions: TerminalAction) {
    Row {
        Text("Actions: ", style = TextStyle.Dim)
        val actionsText = actions.joinToString { "${it.name} (${it.keycodes.joinToString()}})" }
        Text(actionsText)
    }
    val terminal = LocalTerminal.current
    DisposableEffect(actions) {
        terminal.
        onDispose {

        }
    }
}