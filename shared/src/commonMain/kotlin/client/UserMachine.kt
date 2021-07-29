package client

import User
import io.ktor.client.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import serverURL

data class UserMachine(
    val tokenStorage: TokenStorage = MemoryTokenStorage(),
    val httpClient: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
) {
    val currentUser: Flow<User?> = tokenStorage.currentUser
    suspend fun loadTokens(): BearerTokens {
        val saved = tokenStorage.getSavedTokens()
        if (saved != null) return saved.toBearerTokens()
        val (accessJWT, refreshJWT) = httpClient.get<Pair<String, String>>("$serverURL/auth/anonymous")
        val tokens = BearerTokens(accessJWT, refreshJWT)
        tokenStorage.saveTokens(tokens.toAuthTokens())
        println("saved new tokens = ${tokenStorage.getSavedTokens()}")
        return tokens
    }
    suspend fun refreshTokens(): BearerTokens? {
        val saved = tokenStorage.getSavedTokens() ?: return null
        val (accessJWT, refreshJWT) = httpClient.get<Pair<String, String>>("$serverURL/auth/refresh") {
            header("Authorization: Bearer", saved.refreshToken)
        }
        val tokens = BearerTokens(accessJWT, refreshJWT)
        tokenStorage.saveTokens(tokens.toAuthTokens())
        println("saved refreshed tokens = ${tokenStorage.getSavedTokens()}")
        return tokens
    }
}