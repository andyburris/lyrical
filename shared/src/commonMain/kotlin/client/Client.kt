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

data class Client(val httpClient: HttpClient = defaultHttpClient()) {
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

fun defaultHttpClient(): HttpClient {
    lateinit var client: HttpClient
    client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Auth) {
            bearer {
                realm = "com.andb.apps.lyrical"
                loadTokens {
                    val (accessToken, refreshToken) = client.get<Pair<String, String>>("/auth/anonymous")
                    BearerTokens(accessToken, refreshToken)
                }
                refreshTokens { response ->
                    val (accessToken, refreshToken) = client.get<Pair<String, String>>("/auth/refresh")
                    BearerTokens(accessToken, refreshToken)
                }
            }
        }
    }
    return client
}