package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
    onOpenTest: () -> Unit,
    onCloseLyrical: () -> Unit,
) {
    Column {
        AppBar("Lyrical")
        Divider()

        val selectedOptionIndex = remember { mutableStateOf(0) }
        val selectedOption = derivedStateOf { HomeOptions.values()[selectedOptionIndex.value] }
        Text("Play", style = TextStyle.Dim)
        Text(if (selectedOption.value == HomeOptions.CreateGame) "> Create Game" else "Create Game")
        Text(if (selectedOption.value == HomeOptions.JoinRoom) "> Join Room" else "Join Room")
        Text(if (selectedOption.value == HomeOptions.Test) "> Test" else "Test")
        Divider()
        Actions(
            TerminalAction("Select Option", Key.DirectionUp.pressed(), Key.DirectionDown.pressed()) {
                when(it.key) {
                    Key.DirectionUp -> selectedOptionIndex.value = (selectedOptionIndex.value - 1).coerceAtLeast(0)
                    Key.DirectionDown -> selectedOptionIndex.value = (selectedOptionIndex.value + 1).coerceAtMost(2)
                }
            },
            TerminalAction("Choose Option", Key.Enter.pressed()) {
                when(selectedOption.value) {
                    HomeOptions.CreateGame -> onCreateGame.invoke()
                    HomeOptions.JoinRoom -> onOpenJoinRoom.invoke()
                    HomeOptions.Test -> onOpenTest.invoke()
                }
            },
            TerminalAction("Close Lyrical", Key.CtrlLeft + Key.C) { onCloseLyrical.invoke() }
        )
    }
}

private enum class HomeOptions {
    CreateGame,
    JoinRoom,
    Test
}