package room.lobby

import SpotifyRepository
import androidx.compose.runtime.*
import client.ChoosePlaylistsMachine
import client.PlaylistSearchState
import client.SelectedPlaylist
import client.openSpotifyLogin
import common.Icon
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifier.clickable
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import model.GenericPlaylist
import org.jetbrains.compose.common.ui.*
import org.jetbrains.compose.common.ui.draw.clip
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
            onLoginClick = { openSpotifyLogin() },
            onSelect = onSelect
        )
        PlaylistSection(
            name = "Spotify Playlists",
            searchState = spotifyPlaylists.value,
            onLoginClick = { openSpotifyLogin() },
            onSelect = onSelect
        )
    }
}

@Composable
private fun SearchBar(term: String, modifier: Modifier = Modifier, onTermUpdate: (String) -> Unit) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(CurrentPalette.backgroundDark)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        //TODO: add Arrangement.SpacedBy(16.dp)
    ) {
        Icon(Icon.Search, contentDescription = null, tint = CurrentPalette.contentSecondary)
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
