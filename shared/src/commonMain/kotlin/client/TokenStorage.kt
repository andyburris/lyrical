package client

import User
import com.adamratzman.spotify.utils.Language
import io.ktor.client.features.auth.providers.*
import io.ktor.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)
fun BearerTokens.toAuthTokens() = AuthTokens(accessToken, refreshToken)
fun AuthTokens.toBearerTokens() = BearerTokens(accessToken, refreshToken)

interface TokenStorage {
    val currentUser: Flow<User?>
    fun getSavedTokens(): AuthTokens?
    fun saveTokens(tokens: AuthTokens)
}

class MemoryTokenStorage : TokenStorage {
    private var token: AuthTokens? = null
    private val userFlow = MutableStateFlow<User?>(null)
    override fun getSavedTokens(): AuthTokens? = token
    override val currentUser: Flow<User?> = userFlow

    override fun saveTokens(tokens: AuthTokens) {
        token = tokens
        userFlow.value = tokens.decodeUser()
    }

}

@OptIn(InternalAPI::class)
fun AuthTokens.decodeUser(): User {
    val middleSection = this.accessToken.split(".")[1]
    val serialized = middleSection.decodeBase64String()
    val id = serialized.removeSuffix("\"}").takeLastWhile { it != '\"' }
    val isAnonymous = true
    return when(isAnonymous) {
        true -> User.Anonymous(id)
        false -> User.Spotify(id)
    }
}