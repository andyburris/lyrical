package room.lobby

import androidx.compose.runtime.Composable
import platform.LyricalTheme
import platform.Text
import jetbrains.compose.common.shapes.CircleShape
import model.GenericPlaylist
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.size
import org.jetbrains.compose.common.ui.unit.dp

@Composable
fun VerticalPlaylistItem(playlist: GenericPlaylist, modifier: Modifier = Modifier, selected: Boolean = false) {
    Column(modifier) { //TODO: Add Arrangement.SpacedBy(12.dp)
        Box(
            modifier = Modifier
                .clip(LyricalTheme.shapes.small)
                .background(LyricalTheme.colors.overlay)
                .size(128.dp)
        ) {
            //TODO: Add Image
            if (selected) {
                //TODO: Add check icon
            }
        }
        Column {
            Text(playlist.name, style = LyricalTheme.typography.subtitle2)
            Text(playlist.subtitle, style = LyricalTheme.typography.body2)
        }
    }
}

@Composable
fun VerticalPlaylistPlaceholderSmall(modifier: Modifier = Modifier) {
    Column(modifier) { //TODO: Add Arrangement.SpacedBy(12.dp)
        Box(
            modifier = Modifier
                .clip(LyricalTheme.shapes.small)
                .background(LyricalTheme.colors.overlay)
                .size(72.dp)
        ) {}
        Column {
            Box(Modifier.clip(CircleShape).background(LyricalTheme.colors.overlay).size(width = 48.dp, height = 8.dp)) {}
            Box(Modifier.clip(CircleShape).background(LyricalTheme.colors.overlay).size(width = 32.dp, height = 8.dp)) {}
        }
    }
}

@Composable
fun VerticalPlaylistPlaceholderLarge(modifier: Modifier = Modifier) {
    Column(modifier) { //TODO: Add Arrangement.SpacedBy(12.dp)
        Box(Modifier.clip(LyricalTheme.shapes.small).background(LyricalTheme.colors.overlay).size(128.dp)) {}
        Column {
            Box(Modifier.clip(CircleShape).background(LyricalTheme.colors.overlay).size(width = 72.dp, height = 16.dp)) {}
            Box(Modifier.clip(CircleShape).background(LyricalTheme.colors.overlay).size(width = 64.dp, height = 16.dp)) {}
        }
    }
}

