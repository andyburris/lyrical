import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import common.Actions
import common.AppBar
import common.Divider
import data.Key
import data.TerminalAction
import data.pressed
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
        Text("Input")
        Actions(
            TerminalAction("Join Room", Key.Enter.pressed()) {
                val inputCode = input.value
                if (inputCode.isValidRoomCode()) onJoinRoom.invoke(inputCode)
            },
            TerminalAction("Back to Home", Key.Escape.pressed()) {
                onBack.invoke()
            }
        )
    }
}