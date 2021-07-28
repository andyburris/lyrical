package room.lobby

import SpotifyRepository
import androidx.compose.runtime.*
import client.ChoosePlaylistsMachine
import client.PlaylistSearchState
import client.SelectedPlaylist
import kotlinx.coroutines.flow.Flow
import model.GenericPlaylist
import org.jetbrains.compose.common.foundation.clickable
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp
import platform.*

@Composable
fun ChoosePlaylists(selectedPlaylists: List<GenericPlaylist>, spotifyRepository: SpotifyRepository, modifier: Modifier = Modifier, onSelect: (GenericPlaylist) -> Unit) {
    val selectedPlaylistsFlow = snapshotFlow { selectedPlaylists }
    val spotifyRepositoryFlow: Flow<SpotifyRepository> = snapshotFlow { spotifyRepository }
    val coroutineScope = rememberCoroutineScope()
    val choosePlaylistsMachine = remember { ChoosePlaylistsMachine(spotifyRepositoryFlow, selectedPlaylistsFlow, coroutineScope) }
    val searchTerm = choosePlaylistsMachine.searchTerm.collectAsState()
    val userPlaylists = choosePlaylistsMachine.filteredUserPlaylists.collectAsState()
    val spotifyPlaylists = choosePlaylistsMachine.searchedSpotifyPlaylists.collectAsState()

    Column(modifier) { //TODO: add Arrangement.SpacedBy(32.dp)
        SearchBar(term = searchTerm.value) { choosePlaylistsMachine.handleSearchUpdate(it) }
        PlaylistSection(
            name = "My Playlists",
            searchState = userPlaylists.value,
            onLoginClick = { },
            onSelect = onSelect
        )
        PlaylistSection(
            name = "Spotify Playlists",
            searchState = spotifyPlaylists.value,
            onLoginClick = { },
            onSelect = onSelect
        )
    }
}

@Composable
private fun SearchBar(term: String, modifier: Modifier = Modifier, onTermUpdate: (String) -> Unit) {
    Box(modifier = modifier.padding(16.dp)) {
        TextField(term, onTermUpdate, placeholder = "Search playlists, artists...")
    }
}

@Composable
private fun PlaylistSection(name: String, searchState: PlaylistSearchState, modifier: Modifier = Modifier, onLoginClick: () -> Unit, onSelect: (GenericPlaylist) -> Unit) {
    when(searchState) {
        is PlaylistSearchState.Results -> PlaylistResultsSection(name, searchState.playlists, modifier, onSelect = onSelect)
        PlaylistSearchState.RequiresLogin -> PlaylistRequiresLoginSection(name, modifier, onLoginClick = onLoginClick)
        PlaylistSearchState.Loading -> PlaylistLoadingSection(name, modifier)
        PlaylistSearchState.Error -> {}
    }
}

@Composable
private fun PlaylistResultsSection(name: String, playlists: List<SelectedPlaylist>, modifier: Modifier = Modifier, onSelect: (GenericPlaylist) -> Unit) {
    Column(modifier = modifier) { //TODO: add Arrangement.SpacedBy(16.dp)
        Text(name.uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackgroundSecondary)
        HorizontalOverflowRow(Modifier.fillMaxWidth()) { //TODO: add Arrangement.SpacedBy(24.dp) (or 16?)
            playlists.forEach {
                VerticalPlaylistItem(it.playlist, selected = it.selected, modifier = Modifier.clickable { onSelect.invoke(it.playlist)})
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
            //.clip(LyricalTheme.shapes.medium) TODO: readd once shapes are supported
            .background(LyricalTheme.colors.backgroundDark)
            .padding(32.dp)
    ) { //TODO: Arrangement.SpacedBy(32.dp)
        Text(name.uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackgroundSecondary)
        HorizontalOverflowRow(Modifier.fillMaxWidth()) { //TODO: add Arrangement.SpacedBy(24.dp) (or 16?)
            repeat(5) { //TODO: add constraints to limit overflow for web
                VerticalPlaylistPlaceholderSmall()
            }
        }
        Text("Log in with Spotify to easily access your playlists, save your progress, and compete with others!")
        Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
            Text("Log in with Spotify".uppercase())
        }
    }
}
