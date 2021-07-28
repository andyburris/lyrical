package client

import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.getSpotifyPkceAuthorizationUrl
import com.adamratzman.spotify.getSpotifyPkceCodeChallenge
import com.adamratzman.spotify.spotifyClientPkceApi
import spotifyClientID

fun getSpotifyLoginPKCE(): String {
    val codeVerifier = ""
    val codeChallenge = getSpotifyPkceCodeChallenge(codeVerifier)
    return getSpotifyPkceAuthorizationUrl(
        SpotifyScope.PLAYLIST_READ_PRIVATE,
        clientId = spotifyClientID,
        redirectUri = redirectURI,
        codeChallenge = codeChallenge
    )
}

fun handleSpotifyLoginPKCE(code: String) {
    val api = spotifyClientPkceApi(
        clientId = spotifyClientID,
        redirectUri = redirectURI,
        authorizationCode = code,
        pkceCodeVerifier = ""
    )
}

val redirectURI: String = ""