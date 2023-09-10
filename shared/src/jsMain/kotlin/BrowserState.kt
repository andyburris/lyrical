import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.spotifyAppApi
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.random.Random

class BrowserHomeMachine(
    coroutineScope: CoroutineScope,
    onStartGame: (List<SimplePlaylist>, GameConfig) -> Unit
) : HomeMachine() {
    private val spotifyRepository = MutableStateFlow(getRepository { this.handleAuthAction(it) })
    override val homeState: StateFlow<Screen.Home> = spotifyRepository
        .map { it.toHomeScreen() }
        .stateIn(coroutineScope, SharingStarted.Eagerly, spotifyRepository.value.toHomeScreen())
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
                    spotifyRepository.value = getRepository { this.handleAuthAction(it) }
                }
            }
        }
    }

    override fun handleAction(authAction: SetupAction) {

    }
}

private fun SpotifyRepository.toHomeScreen() = when(this) {
    is SpotifyRepository.LoggedIn -> Screen.Home.LoggedIn(this, emptyList()) // TODO: load from local storage
    SpotifyRepository.LoggedOut -> Screen.Home.LoggedOut
}