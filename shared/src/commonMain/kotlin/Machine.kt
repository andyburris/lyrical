import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable

abstract class SetupMachine(
    coroutineScope: CoroutineScope,
    initialRepository: SpotifyRepository,
    initialSetupState: SetupState?,
) {
    protected val spotifyRepository = MutableStateFlow(initialRepository)
    protected val setupState = MutableStateFlow(initialSetupState)
    val homeScreen: StateFlow<Screen.Home> = combine(spotifyRepository, setupState) { repo, setupState ->
        repo.toHomeScreen(setupState)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, spotifyRepository.value.toHomeScreen(setupState.value))

    abstract fun handleAuthAction(authAction: AuthAction)
    abstract fun handleStart(setupState: SetupState): String
    open fun onChangeSetupState(setupState: SetupState) {
        this.setupState.value = setupState
    }
}

private fun SpotifyRepository.toHomeScreen(setupState: SetupState?) = when(this) {
    is SpotifyRepository.LoggedIn -> Screen.Home.LoggedIn(this, setupState ?: SetupState(emptyList(), GameOptions()))
    else -> Screen.Home.LoggedOut
}

abstract class GameMachine(
    coroutineScope: CoroutineScope,
    private val spotifyRepository: SpotifyRepository.LoggedIn,
    private val lyricsRepository: LyricsRepository,
    val gameID: String,
    val playlistIDs: List<String>,
    val options: GameOptions,
    val initialGameState: GameState,
) {
    protected val gameState = MutableStateFlow(initialGameState)
    val gameScreen: StateFlow<Screen.GameScreen> = gameState.map {
        it.toGameScreen()
    }.stateIn(coroutineScope, SharingStarted.Eagerly, gameState.value.toGameScreen())

    val suggestions = MutableStateFlow(emptyList<SuggestionTrack>())

    init {
        if (initialGameState is GameState.Loading) {
            loadSongs()
        }
    }

    open fun onLoadGame(game: Game) {
        gameState.value = GameState.Playing(game, GameScreen.Question)
    }

    open fun onSongsLoaded(songs: List<SourcedTrack>) {
        gameState.value = GameState.Loading.Lyrics(songs)
        loadLyrics(songs)
    }

    open fun handleAction(action: GameAction) {
        println("handling action, current gameState = ${gameState.value}")
        when (action) {
            is GameAction.AnswerQuestion -> gameState.value = when (val gameState = gameState.value) {
                is GameState.Loading -> throw Error("Can't answer when gameState = $gameState")
                is GameState.Playing -> gameState.copy(game = gameState.game.withNextAnswer(action.answer), GameScreen.Answer)
            }
            is GameAction.NextQuestion -> gameState.value = when (val gameState = gameState.value) {
                is GameState.Loading -> throw Error("Can't move to next question when gameState = $gameState")
                is GameState.Playing -> gameState.copy(screen = if (gameState.game.isEnded) GameScreen.End else GameScreen.Question)
            }
            is GameAction.RequestHint -> gameState.value = when (val gameState = gameState.value) {
                is GameState.Loading -> throw Error("Can't request hint when gameState = $gameState")
                is GameState.Playing -> gameState.copy(game = gameState.game.withHintUsed(action.hint), GameScreen.Question)
            }
            is GameAction.Reload -> gameState.value = when(val gameState = gameState.value) {
                is GameState.Loading.Error.Songs -> {
                    loadSongs()
                    GameState.Loading.Songs
                }
                is GameState.Loading.Error.Lyrics -> {
                    loadLyrics(gameState.loadedSongs)
                    GameState.Loading.Lyrics(gameState.loadedSongs)
                }
                else -> throw Error("Can't reload unless in GameState.Loading.Error")
            }
        }
        println("handled action, new gameState = ${gameState.value}")
    }

    private fun loadLyrics(songs: List<SourcedTrack>) {
        CoroutineScope(Dispatchers.Default).launch {
            runCatching {
                val tracksWithLyrics = lyricsRepository.getLyricsFor(songs)
                val game = Game(tracksWithLyrics.generateQuestions(options), options, suggestions.value)
                game
            }.onSuccess {
                onLoadGame(it)
            }.onFailure {
                gameState.value = GameState.Loading.Error.Lyrics(songs)
            }
        }
    }

    private fun loadSuggestions(allTracks: List<Track>) {
        CoroutineScope(Dispatchers.Default).launch {
            val searchResults = allTracks.getAllSearchResults(spotifyRepository)
        }
    }

    private fun loadSongs() {
        CoroutineScope(Dispatchers.Default).launch {
            runCatching {
                val playlists = playlistIDs.mapNotNull { spotifyRepository.getPlaylistByURI(it) }
                val allTracks = playlists.getAllSongsFromPlaylists(spotifyRepository)
                suggestions.value = allTracks.map { sourcedTrack -> sourcedTrack.track.let { SuggestionTrack(it.id, it.name, it.artists.map { it.name }, it.album) } }
                val randomTracks = playlists.getRandomSongs(allTracks, options)
                if (randomTracks.isEmpty()) throw Error("No songs loaded")
                randomTracks
            }.onSuccess {
                onSongsLoaded(it)
            }.onFailure {
                gameState.value = GameState.Loading.Error.Songs
            }
        }
    }
}

private fun GameState.toGameScreen(): Screen.GameScreen = when(this) {
    is GameState.Loading -> Screen.GameScreen.Loading(when(this) {
        is GameState.Loading.Error -> LoadingState.ErrorLoading
        is GameState.Loading.Lyrics -> LoadingState.LoadingLyrics
        GameState.Loading.Songs -> LoadingState.LoadingSongs
    })
    is GameState.Playing -> when(this.screen) {
        GameScreen.Question -> Screen.GameScreen.Question(this.game.questions.indexOfFirst { it.answer is GameAnswer.Unanswered }, this.game)
        GameScreen.Answer -> Screen.GameScreen.Answer(this.game.questions.indexOfLast { it.answer !is GameAnswer.Unanswered }, this.game)
        GameScreen.End -> Screen.GameScreen.End(this.game)
    }
}

suspend fun List<Track>.toSourcedTracks(sourcePlaylist: SimplePlaylist): List<SourcedTrack> {
    return this.map { it.toSourcedTrack(sourcePlaylist) }
}

fun Track.toSourcedTrack(sourcePlaylist: SimplePlaylist) = SourcedTrack(this, sourcePlaylist)

suspend fun List<SimplePlaylist>.getAllSongsFromPlaylists(
    spotifyRepository: SpotifyRepository.LoggedIn,
): List<SourcedTrack> {
    return this
        .map {
            CoroutineScope(Dispatchers.Default).async {
                spotifyRepository.getPlaylistTracks(it.uri.uri).toSourcedTracks(it)
            }
        }
        .awaitAll()
        .flatten()
}

fun List<SimplePlaylist>.getRandomSongs(
    allTracks: List<SourcedTrack>,
    config: GameOptions,
): List<SourcedTrack> {
    println("getting random songs, config = $config")
    return if (config.distributePlaylistsEvenly) {
        val songsPerPlaylist: Map<SimplePlaylist, Int> = this.distributeEvenly(config.amountOfSongs)

        val selectedTracks = mutableListOf<SourcedTrack>()
        allTracks
            .groupBy { it.sourcePlaylist }
            .forEach { (playlist, tracks) ->
            selectedTracks += (tracks - selectedTracks.toSet()).shuffled().take(songsPerPlaylist.getValue(playlist))
        }
        println("selectedTracks = $selectedTracks")
        selectedTracks.shuffled()
    } else {
        allTracks
            .distinctBy { it.track }
            .shuffled()
            .take(config.amountOfSongs)
    }
}

private fun List<SimplePlaylist>.distributeEvenly(amountOfSongs: Int): Map<SimplePlaylist, Int> {
    val amountPerPlaylist = this.zip(amountOfSongs.distributeInto(this.size))
    val (ok, overflowed) = amountPerPlaylist.partition { (playlist, amount) -> amount <= playlist.tracks.total }
    return when {
        overflowed.isEmpty() -> ok.toMap()
        ok.isEmpty() -> throw Error("Must have enough songs in playlists to make game")
        else -> ok
            .map { it.first }
            .distributeEvenly(amountOfSongs - overflowed.sumOf { it.first.tracks.total }) + overflowed
            .map { (p, _) -> p to p.tracks.total }
    }

}

suspend fun List<Track>.getAllSearchResults(spotifyRepository: SpotifyRepository.LoggedIn): List<Track> {
    return this
        .flatMap { it.artists }
        .distinctBy { it.id }
        .map { artist ->
            CoroutineScope(Dispatchers.Default).async { spotifyRepository.getArtistTracks(artist) }
        }
        .awaitAll()
        .flatten()
        .plus(this)
        .distinctBy { it.id }
}

@Serializable
sealed class GameState {
    @Serializable sealed class Loading : GameState() {
        @Serializable sealed class Error : Loading() {
            @Serializable data object Songs : Error()
            @Serializable data class Lyrics(val loadedSongs: List<SourcedTrack>) : Error()
        }
        @Serializable data object Songs : Loading()
        @Serializable data class Lyrics(val loadedSongs: List<SourcedTrack>) : Loading()
    }
    @Serializable data class Playing(val game: Game, val screen: GameScreen) : GameState()
}

enum class GameScreen {
    Question, Answer, End
}
