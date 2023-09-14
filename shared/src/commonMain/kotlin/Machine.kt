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

    init {
        if (initialGameState is GameState.Loading) {
            loadGame()
        }
    }

    open fun onLoadGame(game: Game) {
        gameState.value = GameState.Playing(game, GameScreen.Question)
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
                is GameState.Loading -> when(gameState.state) {
                    LoadingState.ErrorLoading -> {
                        loadGame()
                        GameState.Loading(LoadingState.LoadingSongs)
                    }
                    is LoadingState.LoadingLyrics, LoadingState.LoadingSongs -> throw Error("Can't reload unless in LoadingState.Error")
                }
                else -> throw Error("Can't reload unless in LoadingState.Error")
            }
        }
        println("handled action, new gameState = ${gameState.value}")
    }

    private fun loadGame() {
        CoroutineScope(Dispatchers.Default).launch {
            runCatching {
                val playlists = playlistIDs.mapNotNull { spotifyRepository.getPlaylistByURI(it) }
                val randomTracks = playlists.getRandomSongs(spotifyRepository, options)
                gameState.value = GameState.Loading(LoadingState.LoadingLyrics(0, options.amountOfSongs))
                val tracksWithLyrics = lyricsRepository.getLyricsFor(randomTracks)
                println("after load lyrics")
                val game = Game(tracksWithLyrics.generateQuestions(options), options)
                game
            }.onSuccess {
                onLoadGame(it)
            }.onFailure {
                gameState.value = GameState.Loading(LoadingState.ErrorLoading)
            }
        }
    }
}

private fun GameState.toGameScreen(): Screen.GameScreen = when(this) {
    is GameState.Loading -> Screen.GameScreen.Loading(this.state)
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

suspend fun List<SimplePlaylist>.getRandomSongs(
    spotifyRepository: SpotifyRepository.LoggedIn,
    config: GameOptions,
): List<SourcedTrack> {
    println("getting random songs, config = $config")
    return if (config.distributePlaylistsEvenly) {
        val songsPerPlaylist = this.distributeEvenly(config.amountOfSongs)
        val playlistTracks: List<Pair<SimplePlaylist, List<SourcedTrack>>> = this
            .map {
                CoroutineScope(Dispatchers.Default).async {
                    it to spotifyRepository.getPlaylistTracks(it.uri.uri).toSourcedTracks(it)
                }
            }
            .awaitAll()
        val allTracks = mutableListOf<SourcedTrack>()
        playlistTracks.forEach { (playlist, tracks) ->
            allTracks += (tracks - allTracks.toSet()).shuffled().take(songsPerPlaylist.getValue(playlist))
        }
        allTracks.shuffled()
    } else {
        this
            .flatMap { playlist: SimplePlaylist ->
                spotifyRepository.getPlaylistTracks(playlist.uri.uri).toSourcedTracks(playlist)
            }
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

@Serializable
sealed class GameState {
    @Serializable data class Loading(val state: LoadingState) : GameState()
    @Serializable data class Playing(val game: Game, val screen: GameScreen) : GameState()
}

enum class GameScreen {
    Question, Answer, End
}

@Serializable sealed class LoadingState {
    @Serializable data object ErrorLoading : LoadingState()
    @Serializable data object LoadingSongs : LoadingState()
    @Serializable data class LoadingLyrics(val amountLoaded: Int, val numberOfSongs: Int) : LoadingState() {
        val percent: Double = amountLoaded.toDouble() / numberOfSongs
    }
}
