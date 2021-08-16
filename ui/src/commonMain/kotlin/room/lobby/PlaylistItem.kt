package room.lobby

import androidx.compose.runtime.Composable
import common.Icon
import platform.LyricalTheme
import platform.Text
import jetbrains.compose.common.shapes.CircleShape
import model.GenericPlaylist
import model.name
import model.subtitle
import org.jetbrains.compose.common.foundation.layout.*
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.size
import org.jetbrains.compose.common.ui.unit.dp
import platform.Image
import platform.Resource
import styles.text.TextOverflow

@Composable
fun VerticalPlaylistItem(playlist: GenericPlaylist, modifier: Modifier = Modifier, selected: Boolean = false) {
    Column(modifier) { //TODO: Add Arrangement.SpacedBy(12.dp)
        Box(
            modifier = Modifier
                //.clip(LyricalTheme.shapes.small) TODO: readd once shapes are supported
                .background(LyricalTheme.colors.overlay)
                .size(128.dp)
        ) {
            Image(
                resource = Resource.Url(playlist.imageURL ?: ""), //TODO: replace with file path of placeholder
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().fillMaxHeight(1f) //TODO: replace with fillMaxSize()
            )
            if (selected) {
                Icon(
                    icon = Icon.Check,
                    contentDescription = "Selected",
                    //TODO: add Modifier.align(Alignment.Center)
                )
            }
        }
        Column {
            Text(
                text = playlist.name,
                style = LyricalTheme.typography.subtitle2,
                modifier = Modifier.width(128.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = playlist.subtitle,
                style = LyricalTheme.typography.body2,
                modifier = Modifier.width(128.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun VerticalPlaylistPlaceholderSmall(modifier: Modifier = Modifier) {
    Column(modifier) { //TODO: Add Arrangement.SpacedBy(12.dp)
        Box(
            modifier = Modifier
                //.clip(LyricalTheme.shapes.small) TODO: readd once shapes are supported
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
        Box(
            modifier = Modifier
                //.clip(LyricalTheme.shapes.small) TODO: readd once shapes are supported
                .background(LyricalTheme.colors.overlay)
                .size(128.dp)
        ) {}
        Column {
            Box(Modifier.clip(CircleShape).background(LyricalTheme.colors.overlay).size(width = 72.dp, height = 16.dp)) {}
            Box(Modifier.clip(CircleShape).background(LyricalTheme.colors.overlay).size(width = 64.dp, height = 16.dp)) {}
        }
    }
}

