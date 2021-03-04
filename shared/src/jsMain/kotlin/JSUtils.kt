import com.adamratzman.spotify.SpotifyImplicitGrantApi
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.spotifyImplicitGrantApi
import kotlinx.browser.localStorage
import kotlinx.browser.window
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
    val corsAnywhereURL = "https://cors-anywhere.herokuapp.com/$url"
    this.open("GET", corsAnywhereURL)
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

fun getClientAPIIfLoggedIn(onAuthentication: (AuthAction.Authenticate) -> Unit): SpotifyImplicitGrantApi? {
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
            if (!api.isTokenValid().isValid) onAuthentication.invoke(AuthAction.Authenticate(Random.nextInt().toString()))
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

enum class Platform { Android, IOS, MacOS, Windows, Linux, Other }
val Platform.formattedName get() = when(this) {
    Platform.Android -> "Android"
    Platform.IOS -> "iOS"
    Platform.MacOS -> "macOS"
    Platform.Windows -> "Windows"
    Platform.Linux -> "Linux"
    Platform.Other -> "Other"
}

fun getPlatform(): Platform {
    val macosPlatforms = listOf("Macintosh", "MacIntel", "MacPPC", "Mac68K")
    val windowsPlatforms = listOf("Win32", "Win64", "Windows", "WinCE")
    val iosPlatforms = listOf("iPhone", "iPad", "iPod")
    return when {
        window.navigator.platform in macosPlatforms -> Platform.MacOS
        window.navigator.platform in windowsPlatforms -> Platform.Windows
        window.navigator.platform in iosPlatforms -> Platform.IOS
        window.navigator.userAgent.contains("Android") -> Platform.Android
        window.navigator.userAgent.contains("Linux") -> Platform.Linux
        else -> Platform.Other
    }
}