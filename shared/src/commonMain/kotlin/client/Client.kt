package client

import LocalSpotifyRepository
import RemoteSpotifyRepository
import SpotifyRepository
import UserAction
import io.ktor.client.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import serialize
import server.RoomCode
import serverHost
import serverURL

data class Client(
    val tokenStorage: TokenStorage = MemoryTokenStorage(),
    val httpClient: HttpClient = defaultHttpClient(tokenStorage),
    val spotifyRepository: SpotifyRepository = RemoteSpotifyRepository(httpClient)
) {
    suspend fun createRoom(): RoomCode {
        val url = "$serverURL/creategame"
        println("creating game at $url")
        println("current tokens = ${tokenStorage.getSavedTokens()}")
        return httpClient.post(url)
    }
    fun subscribeToRoom(code: RoomCode, userActions: Flow<UserAction>): Flow<ClientResponse> = flow<ClientResponse> {
        httpClient.webSocket("ws://$serverHost/play/$code") {
            outgoing.send(Frame.Text(UserAction.JoinRoom.serialize()))
            this.launch {
                userActions.collect { action ->
                    outgoing.send(Frame.Text(action.serialize()))
                }
            }
            for (frame in incoming) {
                println("recieved frame = ${(frame as? Frame.Text)?.readText()}")
                when (frame) {
                    is Frame.Text -> {
                        println("decoding client response")
                        val clientResponse = frame.readText().deserializeToClientResponse()
                        println("decoded")
                        println("clientResponse = $clientResponse")
                        emit(clientResponse)
                    }
                }
            }
        }
    }
}

fun defaultHttpClient(tokenStorage: TokenStorage): HttpClient {
    val httpClientNoAuth = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
    return HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Auth) {
            bearer {
                realm = "com.andb.apps.lyrical"
                loadTokens {
                    val saved = tokenStorage.getSavedTokens()
                    if (saved != null) return@loadTokens saved.toBearerTokens()
                    val (accessJWT, refreshJWT) = httpClientNoAuth.get<Pair<String, String>>("$serverURL/auth/anonymous")
                    val tokens = BearerTokens(accessJWT, refreshJWT)
                    tokenStorage.saveTokens(tokens.toAuthTokens())
                    println("saved new tokens = ${tokenStorage.getSavedTokens()}")
                    tokens
                }
                refreshTokens { response ->
                    val saved = tokenStorage.getSavedTokens() ?: return@refreshTokens null
                    val (accessJWT, refreshJWT) = httpClientNoAuth.get<Pair<String, String>>("$serverURL/auth/refresh") {
                        header("Authorization: Bearer", saved.refreshToken)
                    }
                    val tokens = BearerTokens(accessJWT, refreshJWT)
                    tokenStorage.saveTokens(tokens.toAuthTokens())
                    println("saved refreshed tokens = ${tokenStorage.getSavedTokens()}")
                    tokens
                }
            }
        }
        install(WebSockets)
    }
}