import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.spotifyImplicitGrantApi
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.CompletableDeferred
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.get
import org.w3c.dom.parsing.DOMParser
import org.w3c.dom.set
import org.w3c.xhr.XMLHttpRequest
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual suspend fun GeniusRepository.scrapeLyrics(songURL: String): String?  {
    val request = XMLHttpRequest()
    val document = request.get(songURL)
    val lyricsDiv = document.querySelectorAll(".lyrics")[0] ?: document.querySelectorAll(".Lyrics__Container-sc-1ynbvzw-2 jgQsqn")[0] ?: return null
    return (lyricsDiv as Element).innerHTML.replace(unnecessaryScrapingRegex, "").replace("&amp;", "&").split("<br>").filter { it.isNotBlank() }.map { it.trim() }.joinToString("\n")
}

suspend fun XMLHttpRequest.get(url: String): Document = suspendCoroutine { c ->
    this.onload = { statusHandler(this, c) }
    this.open("GET", url)
    this.send()
}

private val parser = DOMParser()
fun statusHandler(xhr: XMLHttpRequest, coroutineContext: Continuation<Document>) {
    if (xhr.readyState == XMLHttpRequest.DONE) {
        if (xhr.status / 100 == 2) {
            coroutineContext.resume(parser.parseFromString(xhr.responseText, "text/html"))
        } else {
            coroutineContext.resumeWithException(RuntimeException("HTTP error: ${xhr.status}"))
        }
    }
}

actual suspend fun SpotifyRepository.userAPI(): SpotifyClientApi? {
    val accessToken = localStorage["access_token"] ?: return null
    val expiresIn = localStorage["access_token_expires_in"]?.toIntOrNull() ?: return null
    val token = Token(accessToken, "Bearer", expiresIn)
    return spotifyImplicitGrantApi(clientID, token)
}

fun authenticateUser() {
    val redirectURL = "http:%2F%2Flocalhost:8080%2Fsetup"
    window.location.href = "https://accounts.spotify.com/authorize?client_id=$clientID&redirect_uri=$redirectURL&scope=playlist-read-private&response_type=token"
}

fun SpotifyRepository.setUserAPI(clientApi: SpotifyClientApi) {
    //apiBacking = CompletableDeferred(clientApi)
}

fun SpotifyRepository.saveAuthentication(token: String, expiresIn: String) {
    localStorage["access_token"] = token
    localStorage["access_token_expires_in"] = expiresIn
}

fun SpotifyRepository.logout() {
    localStorage.removeItem("access_token")
    localStorage.removeItem("access_token_expires_in")
}