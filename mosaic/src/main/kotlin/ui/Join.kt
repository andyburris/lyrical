package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import common.Actions
import common.AppBar
import common.Divider
import data.*
import server.RoomCode
import server.isValidRoomCode

@Composable
fun Join(
    onJoinRoom: (RoomCode) -> Unit,
    onBack: () -> Unit
) {
    Column {
        AppBar("Join Room")
        Divider()
        val input = remember { mutableStateOf("") }
        Text(input.value)
        Actions(
            TerminalAction("Input", *validKeycodes.toTypedArray(), isShown = false) {
                input.value += it.key
            },
            TerminalAction("Backspace", Key.CtrlLeft + Key(nativeKeyCode = 0xbf), isShown = false){
                input.value = input.value.dropLast(1)
            },
            TerminalAction("Join Room", Key.Enter.pressed()) {
                val inputCode = input.value
                if (inputCode.isValidRoomCode()) onJoinRoom.invoke(inputCode)
            },
            TerminalAction("Back to Home", Key.CtrlLeft + Key.H) {
                onBack.invoke()
            }
        )
    }
}

private val validKeycodes = (('0'..'9') + ('a'..'z') + ('A'..'Z')).map { it.toKeycode() }