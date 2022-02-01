package room.lobby

import androidx.compose.runtime.Composable
import common.Icon
import compose.multiplatform.foundation.Image
import compose.multiplatform.foundation.Resource
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.*
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifiers.fillMaxSize
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.text.TextOverflow
import compose.multiplatform.ui.unit.dp
import model.GenericPlaylist
import model.name
import model.subtitle
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.size
import platform.CurrentPalette
import platform.LyricalTheme

@Composable
fun VerticalPlaylistItem(playlist: GenericPlaylist, modifier: Modifier = Modifier, selected: Boolean = false) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(LyricalTheme.shapes.small)
                .background(LyricalTheme.colors.overlay)
                .size(128.dp)
        ) {
            Image(
                resource = Resource.Url(playlist.imageURL ?: ""), //TODO: replace with file path of placeholder
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
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
                color = CurrentPalette.contentSecondary,
                modifier = Modifier.width(128.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun VerticalPlaylistPlaceholderSmall(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) { //TODO: Add
        Box(
            modifier = Modifier
                .clip(LyricalTheme.shapes.small)
                .background(LyricalTheme.colors.overlay)
                .size(72.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(Modifier.clip(CircleShape).background(LyricalTheme.colors.overlay).size(width = 48.dp, height = 8.dp))
            Box(Modifier.clip(CircleShape).background(LyricalTheme.colors.overlay).size(width = 32.dp, height = 8.dp))
        }
    }
}

@Composable
fun VerticalPlaylistPlaceholderLarge(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(LyricalTheme.shapes.small)
                .background(LyricalTheme.colors.overlay)
                .size(128.dp)
        )
        Column {
            Box(Modifier.clip(CircleShape).background(LyricalTheme.colors.overlay).size(width = 72.dp, height = 16.dp))
            Box(Modifier.clip(CircleShape).background(LyricalTheme.colors.overlay).size(width = 64.dp, height = 16.dp))
        }
    }
}

