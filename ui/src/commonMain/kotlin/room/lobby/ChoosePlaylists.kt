package room.lobby

import SpotifyRepository
import androidx.compose.runtime.*
import client.ChoosePlaylistsMachine
import client.PlaylistSearchState
import client.SelectedPlaylist
import platform.Button
import platform.HorizontalOverflowRow
import platform.LyricalTheme
import platform.Text
import kotlinx.coroutines.flow.Flow
import model.GenericPlaylist
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp

@Composable
fun ChoosePlaylists(selectedPlaylists: List<GenericPlaylist>, modifier: Modifier = Modifier) {
    val selectedPlaylistsFlow = snapshotFlow { selectedPlaylists }
    val spotifyRepository: Flow<SpotifyRepository> = snapshotFlow {  }
    val choosePlaylistsMachine = remember { ChoosePlaylistsMachine(selectedPlaylistsFlow) }
    val userPlaylists = choosePlaylistsMachine.filteredUserPlaylists.collectAsState()
    val spotifyPlaylists = choosePlaylistsMachine.searchedSpotifyPlaylists.collectAsState()

    Column(modifier) { //TODO: add Arrangement.SpacedBy(32.dp)
        //TODO: Search bar
        PlaylistSection("My Playlists", userPlaylists.value) {}
        PlaylistSection("Spotify Playlists", spotifyPlaylists.value) {}
    }
}

@Composable
private fun PlaylistSection(name: String, searchState: PlaylistSearchState, modifier: Modifier = Modifier, onLoginClick: () -> Unit) {
    when(searchState) {
        is PlaylistSearchState.Results -> PlaylistResultsSection(name, searchState.playlists, modifier)
        PlaylistSearchState.RequiresLogin -> PlaylistRequiresLoginSection(name, modifier, onLoginClick = onLoginClick)
        PlaylistSearchState.Loading -> PlaylistLoadingSection(name, modifier)
        PlaylistSearchState.Error -> {}
    }
}

@Composable
private fun PlaylistResultsSection(name: String, playlists: List<SelectedPlaylist>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) { //TODO: add Arrangement.SpacedBy(16.dp)
        Text(name.uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackgroundSecondary)
        HorizontalOverflowRow(Modifier.fillMaxWidth()) { //TODO: add Arrangement.SpacedBy(24.dp) (or 16?)
            playlists.forEach {
                VerticalPlaylistItem(it.playlist, selected = it.selected)
            }
        }
    }
}

@Composable
private fun PlaylistLoadingSection(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) { //TODO: add Arrangement.SpacedBy(16.dp)
        Text(name.uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackgroundSecondary)
        HorizontalOverflowRow(Modifier.fillMaxWidth()) { //TODO: add Arrangement.SpacedBy(24.dp) (or 16?)
            repeat(5) {
                VerticalPlaylistPlaceholderLarge()
            }
        }
    }
}

@Composable
private fun PlaylistRequiresLoginSection(name: String, modifier: Modifier = Modifier, onLoginClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(LyricalTheme.shapes.medium)
            .background(LyricalTheme.colors.backgroundDark)
            .padding(32.dp)
    ) { //TODO: Arrangement.SpacedBy(32.dp)
        Text(name.uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackgroundSecondary)
        HorizontalOverflowRow(Modifier.fillMaxWidth()) { //TODO: add Arrangement.SpacedBy(24.dp) (or 16?)
            repeat(5) { //TODO: add constraints to limit overflow for web
                VerticalPlaylistPlaceholderLarge()
            }
        }
        Text("Log in with Spotify to easily access your playlists, save your progress, and compete with others!")
        Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
            Text("Log in with Spotify".uppercase())
        }
    }
}
