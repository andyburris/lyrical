import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class Machine(coroutineScope: CoroutineScope) {
    private val spotifyRepository = SpotifyRepository(clientID, clientSecret)
    private val geniusRepository = GeniusRepository(geniusAPIKey)
    private val navigation = MutableStateFlow<Screen>(Screen.Setup())
    val screen: StateFlow<State> = navigation.transform<Screen, State> { screen ->
        println("transforming screen = $screen")
        when(screen) {
            is Screen.Setup -> {
                val loadingTab = when(val tab = screen.tab) {
                    is Screen.Setup.Tab.SpotifyPlaylists -> State.Setup.TabState.SpotifyPlaylists(tab.searchTerm, PlaylistSearchState.Loading)
                    is Screen.Setup.Tab.MyPlaylists -> State.Setup.TabState.MyPlaylists(tab.searchTerm, PlaylistSearchState.Loading)
                    is Screen.Setup.Tab.URL -> State.Setup.TabState.URL(tab.searchURL, PlaylistSearchState.Loading)
                }
                try {
                    val selectedPlaylists = screen.selectedPlaylistURIs.mapNotNull { spotifyRepository.getPlaylistByURI(it) }
                    emit(State.Setup(selectedPlaylists, screen.config, loadingTab))
                    val loadedTab = when(screen.tab) {
                        is Screen.Setup.Tab.SpotifyPlaylists -> {
                            val playlists = if (screen.tab.searchTerm.isNotBlank()) spotifyRepository.searchPlaylists(screen.tab.searchTerm) else spotifyRepository.getFeaturedPlaylists()
                            State.Setup.TabState.SpotifyPlaylists(screen.tab.searchTerm, PlaylistSearchState.Results(playlists))
                        }
                        is Screen.Setup.Tab.MyPlaylists -> {
                            val playlists = spotifyRepository.getUserPlaylists()
                            val results = playlists?.let { PlaylistSearchState.Results(it) } ?: PlaylistSearchState.RequiresLogin
                            State.Setup.TabState.MyPlaylists(screen.tab.searchTerm, results)
                        }
                        is Screen.Setup.Tab.URL -> {
                            val playlist = spotifyRepository.getPlaylistByURL(screen.tab.searchURL)
                            if (playlist == null) {
                                State.Setup.TabState.URL(screen.tab.searchURL, PlaylistSearchState.Error)
                            } else {
                                State.Setup.TabState.URL(screen.tab.searchURL, PlaylistSearchState.Results(listOf(playlist)))
                            }
                        }
                    }
                    emit(State.Setup(selectedPlaylists, screen.config, loadedTab))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Screen.Loading -> emit(State.Loading)
            is Screen.GameScreen.Question -> emit(State.GameState.Question(screen.questionNumber, screen.data))
            is Screen.GameScreen.Answer -> emit(State.GameState.Answer(screen.questionNumber, screen.data))
            is Screen.GameScreen.End -> emit(State.GameState.End(screen.data))
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, State.Setup(emptyList(), GameConfig(), State.Setup.TabState.SpotifyPlaylists("", PlaylistSearchState.Loading)))

    operator fun plusAssign(action: Action) = applyAction(action)

    fun applyAction(action: Action) {
        when(action) {
            is Action.OpenScreen -> navigation.value = action.screen
            is Action.UpdateScreen -> {
                navigation.value = action.updatedScreen
                println("updated navigation.value to ${navigation.value}")
            }
            is Action.StartGame -> {
                navigation.value = Screen.Loading
                println("Loading tracks")
                CoroutineScope(Dispatchers.Default).launch {
                    val randomTracks = action.playlistURIs.getRandomSongs(action.config)
                    println("Loading lyrics")
                    val tracksWithLyrics = randomTracks.mapNotNull { (track, playlist) ->
                        val lyrics = geniusRepository.getLyrics(track.name, track.artists.map { it.name }) ?: return@mapNotNull null
                        val filteredLyrics = lyrics.lines().filter { !it.startsWith("[") }.distinct()
                        TrackWithLyrics(track, filteredLyrics, playlist.uri.uri)
                    }
                    val game = Game(tracksWithLyrics.toQuestions(), action.config, randomTracks.map { it.second }.distinct())
                    navigation.value = Screen.GameScreen.Question(0, game)
                }
            }
        }
    }

    private suspend fun List<String>.getRandomSongs(config: GameConfig): List<Pair<Track, SimplePlaylist>> {
        val playlists = this.mapNotNull { spotifyRepository.getPlaylistByURI(it) }
        return if (config.distributePlaylistsEvenly) {
            val amountOfSongsPerPlaylist = config.amountOfSongs.distributeInto(this.size)
            val playlistTracks: List<List<Track>> = this.map { spotifyRepository.getPlaylistTracks(it) }
            val allTracks = mutableListOf<Pair<Track, SimplePlaylist>>()
            playlistTracks.forEachIndexed { index, tracks ->
                allTracks += (tracks - allTracks.map { it.first }).shuffled().take(amountOfSongsPerPlaylist[index]).map { Pair(it, playlists[index]) }
            }
            allTracks.shuffled()
        } else {
            this.flatMapIndexed { index: Int, uri: String ->  spotifyRepository.getPlaylistTracks(uri).map { Pair(it, playlists[index]) }.distinct().shuffled().take(config.amountOfSongs) }
        }
    }
}

fun Int.distributeInto(amount: Int): List<Int> {
    val base = this / amount
    val remainder = this % amount
    return (0 until this).mapIndexed { index, i -> if (index < remainder) base + 1 else base }
}