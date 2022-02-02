package client

import ClientSpotifyRepository
import SpotifyRepository
import com.adamratzman.spotify.*
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import kotlinx.coroutines.flow.*
import spotifyClientID
import java.awt.Desktop
import java.io.File
import java.net.URI
import kotlin.random.Random

actual fun openSpotifyLogin() {
    openURLInBrowserTab(getSpotifyLoginPKCE())
}

private val challengeChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + listOf('-', '.', '_', '~')
private val codeVerifier = (0 until Random.nextInt(43, 128)).map { challengeChars.random() }.joinToString("")

fun getSpotifyLoginPKCE(): String {
    val codeVerifier = codeVerifier
    val codeChallenge = getSpotifyPkceCodeChallenge(codeVerifier)
    return getSpotifyPkceAuthorizationUrl(
        SpotifyScope.PLAYLIST_READ_PRIVATE,
        clientId = spotifyClientID,
        redirectUri = redirectURI,
        codeChallenge = codeChallenge
    )
}

suspend fun handleSpotifyLoginPKCE(code: String) = spotifyClientPkceApi(
    clientId = spotifyClientID,
    redirectUri = redirectURI,
    authorizationCode = code,
    pkceCodeVerifier = codeVerifier
).build()

//val redirectURI: String = "lyricalgame://authorize"
val redirectPort = 4324
val redirectURI: String = "http://localhost:$redirectPort/authorize"

actual fun openURLInBrowserTab(url: String) {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
        Desktop.getDesktop().browse(URI(url))
    } else {
        Runtime.getRuntime().exec("x-www-browser $url")
    }
}

actual fun spotifyRepository(defaultRepository: SpotifyRepository): StateFlow<SpotifyRepository> = loginListenerServer(defaultRepository)

/*
fun loginListenerServer(): Flow<SpotifyRepository> = flow {
    embeddedServer(Netty, port = redirectPort) {
        routing {
            get("/authorize") {
                val code = this.call.parameters.getOrFail<String>("code")
                println("authorization code = $code")
                val api = handleSpotifyLoginPKCE(code)
                println("logged into userId = ${api.getUserId()}")
                emit(ClientSpotifyRepository(api))
            }
        }
    }.start(wait = false)
}.map {
    println("emitted repo = $it")
    it
}*/

fun loginListenerServer(defaultRepository: SpotifyRepository): StateFlow<SpotifyRepository>  {
    val stateFlow = MutableStateFlow(defaultRepository)
    embeddedServer(Netty, port = redirectPort) {
        routing {
            get("/authorize") {
                val code = this.call.parameters.getOrFail<String>("code")
                println("authorization code = $code")
                val api = handleSpotifyLoginPKCE(code)
                println("logged into userId = ${api.getUserId()}")
                stateFlow.value = ClientSpotifyRepository(api)
            }
        }
    }.start(wait = false)
    return stateFlow
}
