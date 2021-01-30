import com.adamratzman.spotify.SpotifyImplicitGrantApi
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.spotifyImplicitGrantApi
import kotlinx.browser.localStorage
import kotlinx.coroutines.*
import org.w3c.dom.*
import org.w3c.dom.parsing.DOMParser
import org.w3c.xhr.XMLHttpRequest
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

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

fun getClientAPIIfLoggedIn(onAuthentication: (Action.Authenticate) -> Unit): SpotifyImplicitGrantApi? {
    println("checking for user login")
    val accessToken = localStorage["access_token"] ?: return null
    val tokenType = localStorage["access_token_type"] ?: return null
    val expiresIn = localStorage["access_token_expires_in"] ?: return null
    val token = Token(accessToken, tokenType, expiresIn.toInt())
    println("logged in, checking if valid token")
    return try {
        val api = spotifyImplicitGrantApi(spotifyClientID, token)
        println("created implicitGrantApi")
        CoroutineScope(Dispatchers.Default).launch {
            if (!api.isTokenValid().isValid) onAuthentication.invoke(Action.Authenticate(Random.nextInt().toString()))
        }
        api
    } catch (e: Exception) {
        return null
    }
}


fun logout() {
    localStorage.removeItem("access_token")
    localStorage.removeItem("access_token_expires_in")
}

