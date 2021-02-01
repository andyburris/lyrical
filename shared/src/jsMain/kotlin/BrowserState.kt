import com.adamratzman.spotify.models.SimplePlaylist
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.random.Random

class BrowserState(coroutineScope: CoroutineScope) {
    private val spotifyRepository = MutableStateFlow(getClientAPIIfLoggedIn { handleAction(it) }?.let { SpotifyRepository.LoggedIn(it) } ?: SpotifyRepository.LoggedOut)
    private val lyricsRepository = LyricsRepository()

    private val backingConfig: MutableStateFlow<GameConfig> = MutableStateFlow(savedConfig)
    private val backingPlaylistURIs: MutableStateFlow<List<String>> = MutableStateFlow(savedPlaylistURIs)
    private val backingSearchTerm = MutableStateFlow("")
    private val cachedPlaylists = MutableStateFlow(listOf<SimplePlaylist>())
    private val backingGame: MutableStateFlow<GameState> = MutableStateFlow(GameState.Unstarted)
    val currentGame = backingGame.asStateFlow()

    private val selectedPlaylists = combine(spotifyRepository, backingPlaylistURIs, cachedPlaylists) { repository, playlistURIs, cachedPlaylists ->
        if (repository !is SpotifyRepository.LoggedIn) return@combine null
        playlistURIs.mapNotNull { uri ->
            cachedPlaylists.find { it.uri.uri == uri } ?: repository.getPlaylistByURI(uri)
        }
    }

    private val searchedPlaylists = combineTransform(spotifyRepository, backingSearchTerm) { repository, term ->
        if (repository !is SpotifyRepository.LoggedIn) return@combineTransform
        println("transforming term = $term")
        val playlists = when {
            term.isNotBlank() -> (repository.searchPlaylists(term) + repository.getPlaylistByURL(term)).filterNotNull()
            else -> repository.getFeaturedPlaylists()
        }
        cachedPlaylists.value = (cachedPlaylists.value + playlists).distinct()
        emit(playlists)
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val filteredUserPlaylists = combineTransform(spotifyRepository, backingSearchTerm) { repository, term ->
        if (repository !is SpotifyRepository.LoggedIn) return@combineTransform
        val playlists = repository.getUserPlaylists().filter { term in it.name }
        cachedPlaylists.value = (cachedPlaylists.value + playlists).distinct()
        emit(playlists)
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val addPlaylistState = combineTransform(spotifyRepository, backingSearchTerm, searchedPlaylists, filteredUserPlaylists, backingPlaylistURIs) { repository, term, searchedPlaylists, userPlaylists, uris ->
        if (repository !is SpotifyRepository.LoggedIn) return@combineTransform
        val searchedResults = when(searchedPlaylists) {
            null -> PlaylistSearchState.Loading
            else -> PlaylistSearchState.Results(searchedPlaylists.map { it to (it.uri.uri in uris) })
        }
        val userResults = when (userPlaylists) {
            null -> PlaylistSearchState.Loading
            else -> PlaylistSearchState.Results(userPlaylists.map { it to (it.uri.uri in uris) })
        }
        emit(State.Setup.AddPlaylistState(term, searchedResults, userResults))
    }.stateIn(coroutineScope, SharingStarted.Lazily, State.Setup.AddPlaylistState("", PlaylistSearchState.Loading, PlaylistSearchState.Loading))

    val currentSetupScreen = combine(spotifyRepository, backingConfig, selectedPlaylists, addPlaylistState) { spotifyRepository, config, selectedPlaylists, addPlaylistState ->
        when {
            spotifyRepository is SpotifyRepository.LoggedOut || selectedPlaylists == null -> null
            spotifyRepository is SpotifyRepository.LoggedIn -> State.Setup(selectedPlaylists = selectedPlaylists, config, addPlaylistState)
            else -> null
        }
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
                is GameState.Playing -> gameState.copy(game = gameState.game.withNextAnswer(action.answer), GameScreen.Answer)
            }
            is GameAction.NextQuestion -> backingGame.value = when (val gameState = backingGame.value) {
                GameState.Unstarted, is GameState.Loading -> throw Error("Can't move to next question when gameState = $gameState")
                is GameState.Playing -> gameState.copy(screen = if (gameState.game.isEnded) GameScreen.End else GameScreen.Question)
            }
            is GameAction.RestartGame -> backingGame.value = GameState.Unstarted
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
                backingGame.value = GameState.Loading(LoadingState.LoadingSongs)
                println("hostname = ${window.location.hostname}, host = ${window.location.host}")
                CoroutineScope(Dispatchers.Default).launch {
                    val spotifyRepository = spotifyRepository.value
                    if (spotifyRepository !is SpotifyRepository.LoggedIn) return@launch
                    val randomTracks = action.playlists.getRandomSongs(spotifyRepository, action.config)
                    backingGame.value = GameState.Loading(LoadingState.LoadingLyrics(0, action.config.amountOfSongs))
                    //var amountLoaded = 0
                    val tracksWithLyrics = lyricsRepository.getLyricsFor(randomTracks)
                    val game = Game(tracksWithLyrics.toQuestions(), action.config)
                    backingGame.value = GameState.Playing(game, GameScreen.Question)
                }
            }
            is Action.Authenticate -> {
                localStorage["authState"] = Random.nextInt().toString()
                val http = window.location.href.takeWhile { it != ':' }
                val redirectURL = "$http:%2F%2F${window.location.host}%2F%23%2Fauth"
                window.location.href = "https://accounts.spotify.com/authorize?client_id=$spotifyClientID&redirect_uri=$redirectURL&scope=playlist-read-private&response_type=token&state=${localStorage["authState"]}"
            }
            is Action.CheckAuthentication -> {
                println("authenticating response, state = $action.state, saved state = ${localStorage["authState"]}")
                if (action.state == localStorage["authState"]) {
                    localStorage.removeItem("authState")
                    localStorage["access_token"] = action.token
                    localStorage["access_token_type"] = action.type
                    localStorage["access_token_expires_in"] = action.expiresIn.toString()
                    println("saved authentication")
                    spotifyRepository.value = getClientAPIIfLoggedIn { handleAction(it) }?.let { SpotifyRepository.LoggedIn(it) } ?: SpotifyRepository.LoggedOut
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
