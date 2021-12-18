package client

import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.getSpotifyPkceAuthorizationUrl
import com.adamratzman.spotify.getSpotifyPkceCodeChallenge
import com.adamratzman.spotify.spotifyClientPkceApi
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

val redirectURI: String = "lyricalgame://authorize"

actual fun openURLInBrowserTab(url: String) {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
        Desktop.getDesktop().browse(URI(url))
    } else {
        Runtime.getRuntime().exec("x-www-browser $url")
    }
}

