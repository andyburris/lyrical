package home

import androidx.compose.runtime.Composable
import common.Icon
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
import platform.verticalScroll

@Composable
fun HomeScreen(
    onCreateGame: () -> Unit,
    onJoinRoom: () -> Unit
) {
    Column(Modifier.fillMaxWidth().fillMaxWidth().verticalScroll().padding(32.dp)) { //TODO: convert to fillMaxSize, add verticalScroll, add Arrangement.SpacedBy(32.dp)
        HomeHeader(modifier = Modifier.fillMaxWidth())
        Row(Modifier) { // TODO: add height(IntrinsicSize.Min) modifier
            ActionCard(
                title = "Start Game",
                summary = "Pick playlists and artists to play a single or multiplayer game",
                icon = Icon.PlayCircle,
                palette = LyricalTheme.colors.primaryPalette,
                modifier = Modifier //TODO: add shadow, weight, fillMaxHeight Modifier
                    //.clip(LyricalTheme.shapes.medium) TODO: readd once shapes are supported
                    .clickable(onClick = onCreateGame)
            )
            ActionCard(
                title = "Join Room",
                summary = "Have a code? Join someone else’s multiplayer lobby",
                icon = Icon.Join,
                palette = LyricalTheme.colors.backgroundPalette,
                modifier = Modifier //TODO: add shadow, weight, fillMaxHeight Modifier
                    //.clip(LyricalTheme.shapes.medium) TODO: readd once shapes are supported
                    .clickable(onClick = onJoinRoom)
            )
        }
    }
}