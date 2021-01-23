import com.adamratzman.spotify.models.SimplePlaylist
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set

class BrowserState(coroutineScope: CoroutineScope) {

    private val spotifyRepository = SpotifyRepository(clientID, clientSecret)
    private val geniusRepository = GeniusRepository(geniusAPIKey)

    private val backingConfig: MutableStateFlow<GameConfig> = MutableStateFlow(savedConfig)
    private val backingPlaylistURIs: MutableStateFlow<List<String>> = MutableStateFlow(savedPlaylistURIs)
    private val backingSearchTerm = MutableStateFlow("")
    private val cachedPlaylists = MutableStateFlow(listOf<SimplePlaylist>())
    private val backingGame: MutableStateFlow<GameState> = MutableStateFlow(GameState.Unstarted)
    val currentGame = backingGame.asStateFlow()

    private val addPlaylistState = combineTransform(backingSearchTerm, backingPlaylistURIs) { term, uris ->
        println("transforming term = $term, uris = $uris")
        emit(State.Setup.AddPlaylistState(term, PlaylistSearchState.Loading, PlaylistSearchState.RequiresLogin))
        val playlists = when {
            term.isNotBlank() -> (spotifyRepository.searchPlaylists(term) + spotifyRepository.getPlaylistByURL(term)).filterNotNull()
            else -> spotifyRepository.getFeaturedPlaylists()
        }
        cachedPlaylists.value = (cachedPlaylists.value + playlists).distinct()
        val selectedPlaylists = playlists.map { it to (it.uri.uri in uris) }
        emit(State.Setup.AddPlaylistState(term, PlaylistSearchState.Results(selectedPlaylists), PlaylistSearchState.RequiresLogin))
    }.stateIn(coroutineScope, SharingStarted.Lazily, State.Setup.AddPlaylistState("", PlaylistSearchState.Error, PlaylistSearchState.RequiresLogin))

    val currentSetupScreen = combine(backingConfig, backingPlaylistURIs, addPlaylistState) { config, playlistURIs, addPlaylistState ->
        val playlists = playlistURIs.mapNotNull { id ->
            cachedPlaylists.value.find { it.uri.uri == id } ?: spotifyRepository.getPlaylistByURI(id)
        }
        State.Setup(selectedPlaylists = playlists, config, addPlaylistState)
    }.stateIn(coroutineScope, SharingStarted.Lazily, State.Setup(emptyList(), backingConfig.value, addPlaylistState.value))

    val currentLoadingScreen = currentGame.map { gameState ->
        when (gameState) {
            is  GameState.Loading -> gameState.state
            else -> null
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, LoadingState.LoadingSongs)

    val currentQuestionScreen = currentGame.map { gameState ->
        when (gameState) {
            GameState.Unstarted, is GameState.Loading -> null
            is GameState.Playing -> when(gameState.game.questions.any { it.answer is GameAnswer.Unanswered }) {
                true -> State.GameState.Question(gameState.game.questions.indexOfFirst { it.answer is GameAnswer.Unanswered }, gameState.game)
                false -> null
            }
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    val currentAnswerScreen = currentGame.map { gameState ->
        when (gameState) {
            GameState.Unstarted, is GameState.Loading -> null
            is GameState.Playing -> {
                when (gameState.game.questions.any { it.answer !is GameAnswer.Unanswered }) {
                    true -> State.GameState.Answer(gameState.game.questions.indexOfLast { it.answer !is GameAnswer.Unanswered }, gameState.game)
                    false -> null
                }
            }
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    val currentEndScreen = currentGame.map { gameState ->
        when (gameState) {
            GameState.Unstarted, is GameState.Loading -> null
            is GameState.Playing -> {
                when (gameState.game.questions.any { it.answer is GameAnswer.Unanswered }) {
                    true -> null
                    false -> State.GameState.End(gameState.game)
                }
            }
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    fun handleAction(action: Action) {
        when (action) {
            is GameAction.AnswerQuestion -> backingGame.value = when (val gameState = backingGame.value) {
                GameState.Unstarted, is GameState.Loading -> throw Error("Can't answer when gameState = $gameState")
                is GameState.Playing -> gameState.copy(game = gameState.game.withAnswer(action.answer), GameScreen.Answer)
            }
            is GameAction.NextQuestion -> backingGame.value = when (val gameState = backingGame.value) {
                GameState.Unstarted, is GameState.Loading -> throw Error("Can't move to next question when gameState = $gameState")
                is GameState.Playing -> gameState.copy(screen = if (gameState.game.isEnded) GameScreen.End else GameScreen.Question)
            }
            is SetupAction.AddPlaylist -> {
                backingPlaylistURIs.value += action.playlist.uri.uri
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
                backingGame.value = GameState.Loading(LoadingState.LoadingSongs)
                window.location.href = "http://localhost:8080/#/game/loading"
                CoroutineScope(Dispatchers.Default).launch {
                    val playlists = action.playlistURIs.mapNotNull { spotifyRepository.getPlaylistByURI(it) }
                    val randomTracks = playlists.getRandomSongs(spotifyRepository, action.config)
                    println("randomTracks = ${randomTracks.map { it.track.name }}, Loading lyrics")
                    backingGame.value = GameState.Loading(LoadingState.LoadingLyrics(0, action.config.amountOfSongs))
                    var amountLoaded = 0
                    val tracksWithLyrics = randomTracks.mapNotNull { sourcedTrack ->
                        val lyrics = geniusRepository.getLyrics(sourcedTrack.track.name, sourcedTrack.track.artists.map { it.name }) ?: return@mapNotNull null
                        val filteredLyrics = lyrics.lines().filter { !it.startsWith("[") }.distinct()
                        amountLoaded++
                        backingGame.value = GameState.Loading(LoadingState.LoadingLyrics(amountLoaded, action.config.amountOfSongs))
                        TrackWithLyrics(sourcedTrack, filteredLyrics)
                    }
                    val game = Game(tracksWithLyrics.toQuestions(), action.config)
                    println("first question = ${game.questions.first()}")
                    backingGame.value = GameState.Playing(game, GameScreen.Question)
                }
            }
        }
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