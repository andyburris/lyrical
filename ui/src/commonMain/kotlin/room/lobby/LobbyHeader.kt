package room.lobby

import androidx.compose.runtime.Composable
import common.AppBar
import common.Icon
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifier.clickable
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.unit.dp
import model.GenericPlaylist
import org.jetbrains.compose.common.ui.draw.clip
import platform.HorizontalOverflowRow
import platform.LyricalTheme

@Composable
fun LobbyHeader(
    code: String,
    isHost: Boolean,
    playlists: List<GenericPlaylist>,
    modifier: Modifier = Modifier,
    onClickPlaylist: (playlist: GenericPlaylist) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = modifier
            .background(LyricalTheme.colors.backgroundDark)
            .padding(32.dp)
            .padding(bottom = if (isHost) 32.dp else 0.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        AppBar(
            title = "Lobby",
            onNavigateBack = onNavigateBack
        ) {
            ShareRoom(code)
        }
        when {
            playlists.isNotEmpty() -> HorizontalOverflowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 16.dp,
                crossAxisSpacing = 16.dp
            ) {
                playlists.map {
                    val clickableModifier = if (isHost) Modifier.clickable { onClickPlaylist.invoke(it) } else Modifier
                    VerticalPlaylistItem(it, clickableModifier)
                }
            }
            else -> NoPlaylists(isHost)
        }
    }
}

@Composable
private fun ShareRoom(code: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(LyricalTheme.colors.background)
            .padding(start = 16.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(code, style = LyricalTheme.typography.subtitle1)
        Icon(icon = Icon.Share, contentDescription = "Share room code")
    }
}

@Composable
private fun NoPlaylists(isHost: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        HorizontalOverflowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 16.dp,
        ) { //TODO: Add BoxWithConstraints to have enough elements to go off screen?
            repeat(5) {
                VerticalPlaylistPlaceholderSmall()
            }
        }
        Column {
            Text(
                text = if (isHost) "Add Playlists" else "No Playlists",
                style = LyricalTheme.typography.subtitle1,
                color = LyricalTheme.colors.onBackground
            )
            Text(
                text = if (isHost) "Select some playlists or artists to start a game" else "Have the host add some playlists or artists to start a game",
                style = LyricalTheme.typography.body1,
                color = LyricalTheme.colors.onBackgroundSecondary
            )
        }
    }
}