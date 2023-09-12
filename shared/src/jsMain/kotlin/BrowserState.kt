import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.random.Random

private const val setupStateKey = "currentSetupState"

class BrowserHomeMachine(
    coroutineScope: CoroutineScope,
    onReauthenticate: (AuthAction.Authenticate) -> Unit,
) : SetupMachine(
    coroutineScope = coroutineScope,
    initialRepository = getRepository { onReauthenticate(it) },
    initialSetupState = localStorage[setupStateKey]?.let { Json.decodeFromString(it) },
) {
    override fun handleAuthAction(authAction: AuthAction) {
        when(authAction) {
            is AuthAction.Authenticate -> {
                localStorage["authState"] = Random.nextInt().toString()
                val http = window.location.href.takeWhile { it != ':' }
                val redirectURL = "$http:%2F%2F${window.location.host}%2F%23%2Fauth"
                window.location.href = "https://accounts.spotify.com/authorize?client_id=$spotifyClientID&redirect_uri=$redirectURL&scope=playlist-read-private&response_type=token&state=${localStorage["authState"]}"
            }
            is AuthAction.CheckAuthentication -> {
                println("authenticating response, state = $authAction.state, saved state = ${localStorage["authState"]}")
                if (authAction.state == localStorage["authState"]) {
                    localStorage.removeItem("authState")
                    localStorage["access_token"] = authAction.token
                    localStorage["access_token_type"] = authAction.type
                    localStorage["access_token_expires_in"] = authAction.expiresIn.toString()
                    println("saved authentication")
                    spotifyRepository.value = getRepository {
                        this.handleAuthAction(it)
                    }
                }
            }
            is AuthAction.LogOut -> {
                localStorage.removeItem("access_token")
                localStorage.removeItem("access_token_type")
                localStorage.removeItem("access_token_expires_in")
            }
        }
    }

    override fun onChangeSetupState(setupState: SetupState) {
        super.onChangeSetupState(setupState)
        localStorage[setupStateKey] = Json.encodeToString(setupState)
    }

    override fun handleStart(setupState: SetupState): String {
        println("starting game")
        val gameID = generateGameId(existingIds = getAllLocalStorageKeys())
        println("starting game with id = $gameID")
        val storageState = GameStorageState(
            playlistIDs = setupState.selectedPlaylists.map { it.id },
            options = setupState.options,
            currentState = GameState.Loading(LoadingState.LoadingSongs),
        )
        localStorage[gameID] = Json.encodeToString(storageState)
        return gameID
    }
}
private fun getAllLocalStorageKeys(): List<String> = (js("Object.keys(localStorage)") as Array<String>).toList()
private fun generateGameId(existingIds: List<String>): String {
    val generated = (0 until 6).fold("") { acc, _ -> acc + (('a'..'z') - acc).random() }
    return when(generated) {
        in existingIds -> generateGameId(existingIds)
        else -> generated
    }
}

@Serializable
data class GameStorageState(val playlistIDs: List<String>, val options: GameOptions, val currentState: GameState)


class BrowserGameMachine(
    coroutineScope: CoroutineScope,
    spotifyRepository: SpotifyRepository.LoggedIn,
    lyricsRepository: LyricsRepository,
    gameID: String,
    gameStorageState: GameStorageState,
) : GameMachine(
    coroutineScope = coroutineScope,
    spotifyRepository = spotifyRepository,
    lyricsRepository = lyricsRepository,
    gameID = gameID,
    playlistIDs = gameStorageState.playlistIDs,
    options = gameStorageState.options,
    initialGameState = gameStorageState.currentState,
) {
    override fun onLoadGame(game: Game) {
        super.onLoadGame(game)
        localStorage[gameID] = Json.encodeToString(GameStorageState(playlistIDs, options, gameState.value))
    }
    override fun handleAction(action: GameAction) {
        super.handleAction(action)
        localStorage[gameID] = Json.encodeToString(GameStorageState(
            playlistIDs = playlistIDs,
            options = options,
            currentState = gameState.value
        ))
    }
}