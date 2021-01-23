import com.adamratzman.spotify.SpotifyClientApi
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.js.onChangeFunction
import kotlinx.serialization.json.Json
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.parsing.DOMParser
import org.w3c.xhr.XMLHttpRequest
import react.RBuilder
import react.useEffectWithCleanup
import react.useState
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

