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
            coroutineScope.launch {
                val playlists = playlistIDs.mapNotNull { spotifyRepository.getPlaylistByURI(it) }
                val randomTracks = playlists.getRandomSongs(coroutineScope, spotifyRepository, options)
                gameState.value = GameState.Loading(LoadingState.LoadingLyrics(0, options.amountOfSongs))
                val tracksWithLyrics = lyricsRepository.getLyricsFor(randomTracks)
                val game = Game(tracksWithLyrics.generateQuestions(options), options)
                onLoadGame(game)
            }
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
        }
        println("handled action, new gameState = ${gameState.value}")
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
    coroutineScope: CoroutineScope,
    spotifyRepository: SpotifyRepository.LoggedIn,
    config: GameOptions,
): List<SourcedTrack> {
    println("getting random songs, config = $config")
    return if (config.distributePlaylistsEvenly) {
        val amountOfSongsPerPlaylist = config.amountOfSongs.distributeInto(this.size)
        val playlistTracks: List<List<SourcedTrack>> = this
            .map {
                coroutineScope.async {
                    spotifyRepository.getPlaylistTracks(it.uri.uri).toSourcedTracks(it)
                }
            }
            .awaitAll()
        val allTracks = mutableListOf<SourcedTrack>()
        playlistTracks.forEachIndexed { index, tracks ->
            allTracks += (tracks - allTracks).shuffled().take(amountOfSongsPerPlaylist[index])
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

@Serializable
sealed class GameState {
    @Serializable data class Loading(val state: LoadingState) : GameState()
    @Serializable data class Playing(val game: Game, val screen: GameScreen) : GameState()
}

enum class GameScreen {
    Question, Answer, End
}

@Serializable sealed class LoadingState {
    @Serializable data object LoadingSongs : LoadingState()
    @Serializable data class LoadingLyrics(val amountLoaded: Int, val numberOfSongs: Int) : LoadingState() {
        val percent: Double = amountLoaded.toDouble() / numberOfSongs
    }
}
