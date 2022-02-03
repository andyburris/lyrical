package join

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import common.AppBar
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.fillMaxHeight
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.foundation.modifiers.fillMaxSize
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import platform.Button
import platform.LyricalTheme
import platform.TextField
import server.RoomCode

@Composable
fun JoinScreen(
    modifier: Modifier = Modifier,
    onBackHome: () -> Unit,
    onJoinRoom: (RoomCode) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),

    ) {
        AppBar(
            title = "Join Room",
            onNavigateBack = onBackHome,
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val code = remember { mutableStateOf("") }
            Text("Enter Code", style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackground)
            TextField(
                value = code.value,
                onValueChange = { code.value = it },
                textStyle = LyricalTheme.typography.subtitle1
            )
            Button(onClick = { onJoinRoom.invoke(code.value) }, isEnabled = code.value.length == 6) {
                Text("Join")
            }
        }
    }

}