package room.lobby

import androidx.compose.runtime.Composable
import platform.HorizontalOverflowRow
import platform.LyricalTheme
import platform.Text
import model.GenericPlaylist
import org.jetbrains.compose.common.foundation.clickable
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp
import common.AppBar

@Composable
fun LobbyHeader(code: String, isHost: Boolean, playlists: List<GenericPlaylist>, modifier: Modifier = Modifier, onClickPlaylist: (playlist: GenericPlaylist) -> Unit) {
    Column(modifier.padding(32.dp),) { //TODO: Add Arrangement.SpacedBy(32.dp), add 32.dp bottom padding if (isHost)
        AppBar("Lobby") {
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
    Row(modifier.background(LyricalTheme.colors.background).padding(16.dp)) { //TODO: Add CircleShape to background
        Text(code, style = LyricalTheme.typography.subtitle1)
        //TODO: Add share icon
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
                text = if (isHost) "Add Playlists" else "No Playlist",
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