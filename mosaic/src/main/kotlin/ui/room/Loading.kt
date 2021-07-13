package ui.room

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.Text
import common.Actions
import common.AppBar
import common.Divider
import data.Key
import data.LocalTerminal
import data.TerminalAction
import data.plus

@Composable
fun Loading(loadingDescription: String, onLeaveRoom: () -> Unit) {
    AppBar(loadingDescription)
    Divider()
    IndeterminateProgressBar()
    Actions(
        TerminalAction("Leave Room", Key.CtrlLeft + Key.L) {
            onLeaveRoom.invoke()
        }
    )
}

@Composable
private fun IndeterminateProgressBar() {
    val terminalWidth = LocalTerminal.current.terminal.width
    val progressText = (0 until terminalWidth).joinToString("") { "-" }
    Text(progressText)
}