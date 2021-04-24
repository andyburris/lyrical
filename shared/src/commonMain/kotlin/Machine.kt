import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

data class SetupMachine(
    val coroutineScope: CoroutineScope,
    val spotifyRepository: Flow<SpotifyRepository.LoggedIn>,
    val backingConfig: MutableStateFlow<GameConfig>,
    val backingPlaylistURIs: MutableStateFlow<List<String>>,
    val onStartGame: (playlists: List<SimplePlaylist>, config: GameConfig) -> Unit
) {
    private val backingSearchTerm = MutableStateFlow("")

    private val selectedPlaylists = combine(spotifyRepository, backingPlaylistURIs) { repository, playlistURIs ->
        playlistURIs.mapNotNull { repository.getPlaylistByURI(it) }
    }

    @OptIn(ExperimentalTime::class, FlowPreview::class)
    private val searchedPlaylists = combine(spotifyRepository, backingSearchTerm.debounce(500.milliseconds)) { repository, term ->
        when {
            term.isNotBlank() -> (repository.searchPlaylists(term) + repository.getPlaylistByURL(term)).filterNotNull()
            else -> repository.getFeaturedPlaylists()
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val filteredUserPlaylists = combine(spotifyRepository, backingSearchTerm) { repository, term -> repository.getUserPlaylists().filter { term.toLowerCase() in it.name.toLowerCase() } }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val addPlaylistState = combine(backingSearchTerm, searchedPlaylists, filteredUserPlaylists, backingPlaylistURIs) { term, searchedPlaylists, userPlaylists, uris ->
        val searchedResults = when(searchedPlaylists) {
            null -> PlaylistSearchState.Loading
            else -> PlaylistSearchState.Results(searchedPlaylists.map { it to (it.uri.uri in uris) })
        }
        val userResults = when (userPlaylists) {
            null -> PlaylistSearchState.Loading
            else -> PlaylistSearchState.Results(userPlaylists.map { it to (it.uri.uri in uris) })
        }
        return@combine State.Setup.AddPlaylistState(term, searchedResults, userResults)
    }.stateIn(coroutineScope, SharingStarted.Lazily, State.Setup.AddPlaylistState("", PlaylistSearchState.Loading, PlaylistSearchState.Loading))

    val currentSetupScreen = combine(backingConfig, selectedPlaylists, addPlaylistState) { config, selectedPlaylists, addPlaylistState -> State.Setup(selectedPlaylists = selectedPlaylists, config, addPlaylistState) }.stateIn(coroutineScope, SharingStarted.Lazily, State.Setup(emptyList(), backingConfig.value, addPlaylistState.value))

    fun handleAction(action: SetupAction) {
        when(action) {
            is SetupAction.AddPlaylist -> {
                backingPlaylistURIs.value = (backingPlaylistURIs.value + action.playlist.uri.uri).distinct()
                savedPlaylistURIs = backingPlaylistURIs.value.map { it }
            }
            is SetupAction.RemovePlaylist -> {
                backingPlaylistURIs.value -= action.playlist.uri.uri
                savedPlaylistURIs = backingPlaylistURIs.value.map { it }
            }
            is SetupAction.UpdateConfig -> {
                backingConfig.value = action.updatedConfig
                savedConfig = backingConfig.value
            }
            is SetupAction.UpdateSearch -> {
                backingSearchTerm.value = action.searchTerm
            }
            is SetupAction.StartGame -> {
                onStartGame.invoke(action.playlists, backingConfig.value)
            }
        }
    }
}

data class GameMachine(
    val coroutineScope: CoroutineScope,
    val backingGame: MutableStateFlow<GameState>
) {
    val currentGameScreen = backingGame.map { gameState ->
        println("currrentGameScreen updating, gameState = $gameState")
        when(gameState) {
            GameState.Unstarted, is GameState.Loading -> null
            is GameState.Playing -> when(gameState.screen) {
                GameScreen.Question -> State.GameState.Question(gameState.game.questions.indexOfFirst { it.answer is GameAnswer.Unanswered }, gameState.game)
                GameScreen.Answer -> State.GameState.Answer(gameState.game.questions.indexOfLast { it.answer !is GameAnswer.Unanswered }, gameState.game)
                GameScreen.End -> State.GameState.End(gameState.game)
            }
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    fun handleAction(action: GameAction) {
        when (action) {
            is GameAction.AnswerQuestion -> backingGame.value = when (val gameState = backingGame.value) {
                GameState.Unstarted, is GameState.Loading -> throw Error("Can't answer when gameState = $gameState")
                is GameState.Playing -> gameState.copy(game = gameState.game.withNextAnswer(action.answer), GameScreen.Answer)
            }
            is GameAction.NextQuestion -> backingGame.value = when (val gameState = backingGame.value) {
                GameState.Unstarted, is GameState.Loading -> throw Error("Can't move to next question when gameState = $gameState")
                is GameState.Playing -> gameState.copy(screen = if (gameState.game.isEnded) GameScreen.End else GameScreen.Question)
            }
            is GameAction.RestartGame -> backingGame.value = GameState.Unstarted
        }
    }
}

abstract class Machine(
    coroutineScope: CoroutineScope,
    private val lyricsRepository: LyricsRepository,
    val spotifyRepository: MutableStateFlow<SpotifyRepository>,
    val backingConfig: MutableStateFlow<GameConfig>,
    val backingPlaylistURIs: MutableStateFlow<List<String>>,
) {
    private val backingGame: MutableStateFlow<GameState> = MutableStateFlow(GameState.Unstarted)
    val currentGame = backingGame.asStateFlow()

    val setupMachine: SetupMachine = SetupMachine(coroutineScope, spotifyRepository.filterIsInstance<SpotifyRepository.LoggedIn>(), backingConfig, backingPlaylistURIs) { playlists, config ->
        backingGame.value = GameState.Loading(LoadingState.LoadingSongs)
        CoroutineScope(Dispatchers.Default).launch {
            val spotifyRepository = spotifyRepository.value
            if (spotifyRepository !is SpotifyRepository.LoggedIn) return@launch
            val randomTracks = playlists.getRandomSongs(spotifyRepository, config)
            backingGame.value = GameState.Loading(LoadingState.LoadingLyrics(0, config.amountOfSongs))
            val tracksWithLyrics = lyricsRepository.getLyricsFor(randomTracks)
            val game = Game(tracksWithLyrics.generateQuestions(config), config)
            backingGame.value = GameState.Playing(game, GameScreen.Question)
        }
    }

    val gameMachine: GameMachine = GameMachine(coroutineScope, backingGame)

    val currentLoadingScreen = currentGame.map { gameState ->
        when (gameState) {
            is  GameState.Loading -> gameState.state
            else -> null
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, LoadingState.LoadingSongs)

    fun handleAction(action: Action) {
        when (action) {
            is GameAction -> gameMachine.handleAction(action)
            is SetupAction -> setupMachine.handleAction(action)
            is AuthAction -> handleAuthAction(action)
        }
    }
    abstract fun handleAuthAction(authAction: AuthAction)
}

suspend fun List<Track>.toSourcedTracks(sourcePlaylist: SimplePlaylist): List<SourcedTrack> {
    return this.map { SourcedTrack(it, sourcePlaylist) }
}

suspend fun List<SimplePlaylist>.getRandomSongs(spotifyRepository: SpotifyRepository.LoggedIn, config: GameConfig): List<SourcedTrack> {
    println("getting random songs, config = $config")
    return if (config.distributePlaylistsEvenly) {
        val amountOfSongsPerPlaylist = config.amountOfSongs.distributeInto(this.size)
        val playlistTracks: List<List<SourcedTrack>> = this.map { spotifyRepository.getPlaylistTracks(it.uri.uri).toSourcedTracks(it) }
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

sealed class GameState {
    object Unstarted : GameState()
    data class Loading(val state: LoadingState) : GameState()
    data class Playing(val game: Game, val screen: GameScreen) : GameState()
}

enum class GameScreen {
    Question, Answer, End
}

sealed class LoadingState {
    object LoadingSongs : LoadingState()
    data class LoadingLyrics(val amountLoaded: Int, val numberOfSongs: Int) : LoadingState() {
        val percent: Double = amountLoaded.toDouble() / numberOfSongs
    }
}
