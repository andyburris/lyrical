import com.adamratzman.spotify.models.SimplePlaylist
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.random.Random

class BrowserState(coroutineScope: CoroutineScope) : Machine(
    coroutineScope = coroutineScope,
    lyricsRepository = LyricsRepository(),
    spotifyRepository = MutableStateFlow(SpotifyRepository.LoggedOut),
    backingConfig = SourcedMutableStateFlow(savedConfig) { savedConfig = it },
    backingPlaylistURIs = SourcedMutableStateFlow(savedPlaylistURIs) { savedPlaylistURIs = it },
) {
    init {
        println("initializing BrowserState")
        spotifyRepository.value = getClientAPIIfLoggedIn { this.handleAuthAction(it) }?.let { SpotifyRepository.LoggedIn(it) } ?: SpotifyRepository.LoggedOut
    }
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
                    spotifyRepository.value = getClientAPIIfLoggedIn { handleAction(it) }?.let { SpotifyRepository.LoggedIn(it) } ?: SpotifyRepository.LoggedOut
                }
            }
        }
    }
}
