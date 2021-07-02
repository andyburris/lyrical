import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.TextStyle
import common.Actions
import common.AppBar
import common.Divider
import data.Key
import data.TerminalAction
import data.plus
import data.pressed

@Composable
fun HomeScreen(
    onCreateGame: () -> Unit,
    onOpenJoinRoom: () -> Unit,
    onCloseLyrical: () -> Unit,
) {
    Column {
        AppBar("Lyrical")
        Divider()

        val selectedOption = remember { mutableStateOf(PlayOptions.CreateGame) }
        Text("Play", style = TextStyle.Dim)
        Text(if (selectedOption.value == PlayOptions.CreateGame) "> Create Game" else "Create Game")
        Text(if (selectedOption.value == PlayOptions.JoinRoom) "> Join Room" else "Join Room")
        Divider()
        Actions(
            TerminalAction("Select Option", Key.DirectionUp.pressed(), Key.DirectionDown.pressed()) {
                when(it.key) {
                    Key.DirectionUp -> selectedOption.value = PlayOptions.CreateGame
                    Key.DirectionDown -> selectedOption.value = PlayOptions.JoinRoom
                }
            },
            TerminalAction("Choose Option", Key.Enter.pressed()) {
                when(selectedOption.value) {
                    PlayOptions.CreateGame -> onCreateGame.invoke()
                    PlayOptions.JoinRoom -> onOpenJoinRoom.invoke()
                }
            },
            TerminalAction("Close Lyrical", Key.CtrlLeft + Key.Q) { onCloseLyrical.invoke() }
        )
    }
}

private enum class PlayOptions {
    CreateGame,
    JoinRoom
}