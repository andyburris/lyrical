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
import compose.multiplatform.ui.unit.dp
import model.GenericPlaylist
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
        modifier = modifier.background(LyricalTheme.colors.backgroundDark).padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) { //TODO: Add Arrangement.SpacedBy(32.dp), add 32.dp bottom padding if (isHost)
        AppBar(
            title = "Lobby",
            onNavigateBack = onNavigateBack
        ) {
            ShareRoom(code)
        }
        when {
            playlists.isNotEmpty() -> HorizontalOverflowRow(Modifier.fillMaxWidth()) { //TODO: Add Arrangement.SpacedBy(16.dp), add flex row if desktop/web
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
    Row(modifier.background(LyricalTheme.colors.background).padding(16.dp)) { //TODO: Add CircleShape to background, vertical 4.dp padding, right 12.dp padding
        Text(code, style = LyricalTheme.typography.subtitle1)
        Icon(icon = Icon.Share, contentDescription = "Share room code")
    }
}

@Composable
private fun NoPlaylists(isHost: Boolean, modifier: Modifier = Modifier) {
    Column(modifier) { //TODO: Add Arrangement.SpacedBy(32.dp)
        HorizontalOverflowRow(Modifier.fillMaxWidth()) { //TODO: Add Arrangement.SpacedBy(16.dp), BoxWithConstraints to make go off screen?
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