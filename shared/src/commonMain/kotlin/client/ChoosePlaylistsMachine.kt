package client

import ClientSpotifyRepository
import SpotifyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import model.GenericPlaylist
import model.toGenericPlaylist
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

class ChoosePlaylistsMachine(
    private val spotifyRepository: StateFlow<SpotifyRepository>,
    private val selectedPlaylists: Flow<List<GenericPlaylist>>,
    private val coroutineScope: CoroutineScope,
) {
    private val backingSearchTerm = MutableStateFlow("")

    @OptIn(ExperimentalTime::class, FlowPreview::class)
    val searchedSpotifyPlaylists: StateFlow<PlaylistSearchState> = combine(spotifyRepository, selectedPlaylists, backingSearchTerm.debounce(500.milliseconds)) { repository, selected, term ->
        val playlists = when {
            term.isNotBlank() -> repository.searchPlaylists(term)
            else -> repository.getFeaturedPlaylists()
        }.map { it.toGenericPlaylist() }
        PlaylistSearchState.Results(playlists.map { SelectedPlaylist(it, it in selected) })
    }.stateIn(coroutineScope, SharingStarted.Lazily, PlaylistSearchState.Loading)

    val filteredUserPlaylists: StateFlow<PlaylistSearchState> = combine(spotifyRepository, selectedPlaylists, backingSearchTerm) { repository, selected, term ->
        if (repository !is ClientSpotifyRepository) return@combine PlaylistSearchState.RequiresLogin
        val playlists = repository.getUserPlaylists().filter { term.lowercase() in it.name.lowercase() }.map { it.toGenericPlaylist() }
        PlaylistSearchState.Results(playlists.map { SelectedPlaylist(it, it in selected) })
    }.stateIn(coroutineScope, SharingStarted.Lazily, PlaylistSearchState.Loading)

    fun handleSearchUpdate(term: String) {
        backingSearchTerm.value = term
    }
}