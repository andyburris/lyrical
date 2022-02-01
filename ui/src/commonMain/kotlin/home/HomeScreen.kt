package home

import androidx.compose.runtime.Composable
import common.Icon
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.foundation.modifier.clickable
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.foundation.modifier.verticalScroll
import compose.multiplatform.foundation.modifiers.fillMaxSize
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import org.jetbrains.compose.common.ui.draw.clip
import platform.LyricalTheme

@Composable
fun HomeScreen(
    onCreateGame: () -> Unit,
    onJoinRoom: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        HomeHeader(modifier = Modifier.fillMaxWidth())
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) { // TODO: add height(IntrinsicSize.Min) modifier
            ActionCard(
                title = "Start Game",
                summary = "Pick playlists and artists to play a single or multiplayer game",
                icon = Icon.PlayCircle,
                palette = LyricalTheme.colors.primaryPalette,
                modifier = Modifier //TODO: add shadow, weight, fillMaxHeight Modifier
                    .weight(1f)
                    .clip(LyricalTheme.shapes.large)
                    .clickable(onClick = onCreateGame)
            )
            ActionCard(
                title = "Join Room",
                summary = "Have a code? Join someone else’s multiplayer lobby",
                icon = Icon.Join,
                palette = LyricalTheme.colors.backgroundPalette,
                modifier = Modifier //TODO: add shadow, weight, fillMaxHeight Modifier
                    .weight(1f)
                    .clip(LyricalTheme.shapes.large)
                    .clickable(onClick = onJoinRoom)
            )
        }
    }
}