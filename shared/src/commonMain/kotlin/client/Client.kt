package client

import UserAction
import io.ktor.client.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import serialize
import server.RoomCode
import serverURL

data class Client(
    private val tokenStorage: TokenStorage = MemoryTokenStorage(),
    val httpClient: HttpClient = defaultHttpClient(tokenStorage),
) {
    suspend fun createRoom(): RoomCode = httpClient.post("$serverURL/createGame")
    fun subscribeToRoom(code: RoomCode, userActions: Flow<UserAction>): Flow<ClientResponse> = flow<ClientResponse> {
        httpClient.webSocket("$serverURL/play/$code") {
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val clientResponse = Json.decodeFromString(ClientResponse.serializer(), frame.readText())
                        emit(clientResponse)
                    }
                }
            }
            userActions.collect { action ->
                outgoing.send(Frame.Text(action.serialize()))
            }
        }
    }
}

fun defaultHttpClient(tokenStorage: TokenStorage): HttpClient {
    val httpClientNoAuth = HttpClient()
    val httpClientRefreshAuth = HttpClient()
    return HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Auth) {
            bearer {
                realm = "com.andb.apps.lyrical"
                loadTokens {
                    val saved = tokenStorage.getSavedTokens()
                    if (saved != null) return@loadTokens saved
                    val (accessJWT, refreshJWT) = httpClientNoAuth.get<Pair<String, String>>("/auth/anonymous")
                    val tokens = BearerTokens(accessJWT, refreshJWT)
                    tokenStorage.saveTokens(tokens)
                    tokens
                }
                refreshTokens { response ->
                    val saved = tokenStorage.getSavedTokens() ?: return@refreshTokens null
                    val (accessJWT, refreshJWT) = httpClientRefreshAuth.get<Pair<String, String>>("/auth/refresh") {
                        header("Authorization: Bearer", saved.refreshToken)
                    }
                    val tokens = BearerTokens(accessJWT, refreshJWT)
                    tokenStorage.saveTokens(tokens)
                    tokens
                }
            }
        }
    }
}