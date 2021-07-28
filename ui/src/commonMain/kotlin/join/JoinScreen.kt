package join

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import common.AppBar
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.fillMaxHeight
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier
import platform.Button
import platform.LyricalTheme
import platform.Text
import platform.TextField
import server.RoomCode

@Composable
fun JoinScreen(
    modifier: Modifier = Modifier,
    onBackHome: () -> Unit,
    onJoinRoom: (RoomCode) -> Unit
) {
    Column(modifier) {
        AppBar(
            title = "Join Room",
            onNavigateBack = onBackHome,
        )
        Column(Modifier.fillMaxWidth().fillMaxHeight(1f)) {
            val code = remember { mutableStateOf("") }
            Text("Enter Code", style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackground)
            TextField(
                value = code.value,
                onValueChange = { code.value = it },
            )
            Button(onClick = { onJoinRoom.invoke(code.value) }, isEnabled = code.value.length == 6) {
                Text("Join")
            }
        }
    }

}