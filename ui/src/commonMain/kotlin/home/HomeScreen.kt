package home

import androidx.compose.runtime.Composable
import platform.LyricalTheme
import org.jetbrains.compose.common.foundation.clickable
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp

@Composable
fun HomeScreen(
    onCreateGame: () -> Unit,
    onJoinRoom: () -> Unit
) {
    Column(Modifier.fillMaxWidth().fillMaxWidth().padding(32.dp)) { //TODO: convert to fillMaxSize, add verticalScroll, add Arrangement.SpacedBy(32.dp)
        //TODO: header
        Row(Modifier) { // TODO: add height(IntrinsicSize.Min) modifier
            ActionCard(
                title = "Start Game",
                summary = "Pick playlists and artists to play a single or multiplayer game",
                modifier = Modifier //TODO: add shadow, weight, fillMaxHeight Modifier
                    //.clip(LyricalTheme.shapes.medium) TODO: readd once shapes are supported
                    .background(LyricalTheme.colors.primary)
                    .clickable(onClick = onCreateGame)
            )
            ActionCard(
                title = "Join Room",
                summary = "Have a code? Join someone else’s multiplayer lobby",
                modifier = Modifier //TODO: add shadow, weight, fillMaxHeight Modifier
                    //.clip(LyricalTheme.shapes.medium) TODO: readd once shapes are supported
                    .background(LyricalTheme.colors.primary)
                    .clickable(onClick = onJoinRoom)
            )
        }
    }
}