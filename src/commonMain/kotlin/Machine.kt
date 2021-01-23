import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/*class Machine(coroutineScope: CoroutineScope) {
    private val spotifyRepository = SpotifyRepository(clientID, clientSecret)
    private val geniusRepository = GeniusRepository(geniusAPIKey)
    private val navigation = MutableStateFlow<Screen>(Screen.Setup())
    val screen: StateFlow<State> = navigation.transform<Screen, State> { screen ->
        //println("transforming screen = $screen")
        when(screen) {
            is Screen.Setup -> {
                val loadingTab = when(val tab = screen.tab) {
                    is Screen.Setup.Tab.SpotifyPlaylists -> State.Setup.TabState.SpotifyPlaylists(tab.searchTerm, PlaylistSearchState.Loading)
                    is Screen.Setup.Tab.MyPlaylists -> State.Setup.TabState.MyPlaylists(tab.searchTerm, PlaylistSearchState.Loading)
                    is Screen.Setup.Tab.URL -> State.Setup.TabState.URL(tab.searchURL, PlaylistSearchState.Loading)
                }
                emit(State.Setup(screen.selectedPlaylists, screen.config, loadingTab))
                try {
                    val loadedTab = when(screen.tab) {
                        is Screen.Setup.Tab.SpotifyPlaylists -> {
                            val playlists = if (screen.tab.searchTerm.isNotBlank()) spotifyRepository.searchPlaylists(screen.tab.searchTerm) else spotifyRepository.getFeaturedPlaylists()
                            State.Setup.TabState.SpotifyPlaylists(screen.tab.searchTerm, PlaylistSearchState.Results(playlists.map { it to (it in screen.selectedPlaylists) }))
                        }
                        is Screen.Setup.Tab.MyPlaylists -> {
                            val playlists = spotifyRepository.getUserPlaylists()
                            val results = playlists?.let { playlists -> PlaylistSearchState.Results(playlists.map { it to (it in screen.selectedPlaylists) }) } ?: PlaylistSearchState.RequiresLogin
                            State.Setup.TabState.MyPlaylists(screen.tab.searchTerm, results)
                        }
                        is Screen.Setup.Tab.URL -> {
                            val playlist = spotifyRepository.getPlaylistByURL(screen.tab.searchURL)
                            if (playlist == null) {
                                State.Setup.TabState.URL(screen.tab.searchURL, PlaylistSearchState.Error)
                            } else {
                                State.Setup.TabState.URL(screen.tab.searchURL, PlaylistSearchState.Results(listOf(playlist to (playlist in screen.selectedPlaylists))))
                            }
                        }
                    }
                    emit(State.Setup(screen.selectedPlaylists, screen.config, loadedTab))
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
                //println("updated navigation.value to ${navigation.value}")
            }
            is Action.StartGame -> {
                navigation.value = Screen.Loading
                println("Loading tracks")
                CoroutineScope(Dispatchers.Default).launch {
                    val playlists = action.playlistURIs.mapNotNull { spotifyRepository.getPlaylistByURI(it) }
                    val randomTracks = playlists.getRandomSongs(spotifyRepository, action.config)
                    println("randomTracks = ${randomTracks.map { it.track.name }}, Loading lyrics")
                    val tracksWithLyrics = randomTracks.mapNotNull { sourcedTrack ->
                        val lyrics = geniusRepository.getLyrics(sourcedTrack.track.name, sourcedTrack.track.artists.map { it.name }) ?: return@mapNotNull null
                        val filteredLyrics = lyrics.lines().filter { !it.startsWith("[") }.distinct()
                        TrackWithLyrics(sourcedTrack, filteredLyrics)
                    }
                    val game = Game(tracksWithLyrics.toQuestions(), action.config, playlists)
                    navigation.value = Screen.GameScreen.Question(0, game)
                }
            }
        }
    }


}*/


suspend fun List<SimplePlaylist>.getRandomSongs(spotifyRepository: SpotifyRepository, config: GameConfig): List<SourcedTrack> {
    println("getting random songs, config = $config")
    return if (config.distributePlaylistsEvenly) {
        val amountOfSongsPerPlaylist = config.amountOfSongs.distributeInto(this.size)
        val playlistTracks: List<List<Track>> = this.map { spotifyRepository.getPlaylistTracks(it.uri.uri) }
        val allTracks = mutableListOf<SourcedTrack>()
        playlistTracks.forEachIndexed { index, tracks ->
            allTracks += (tracks - allTracks.map { it.track }).shuffled().take(amountOfSongsPerPlaylist[index]).map { SourcedTrack(it, this[index]) }
        }
        allTracks.shuffled()
    } else {
        this.flatMapIndexed { index: Int, playlist: SimplePlaylist ->
            spotifyRepository.getPlaylistTracks(playlist.uri.uri).map { SourcedTrack(it, this[index]) }
        }
            .distinctBy { it.track }
            .shuffled()
            .take(config.amountOfSongs)
    }
}

fun Int.distributeInto(amount: Int): List<Int> {
    val base = this / amount
    val remainder = this % amount
    return (0 until this).mapIndexed { index, i -> if (index < remainder) base + 1 else base }
}