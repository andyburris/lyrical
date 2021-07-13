package client

import io.ktor.client.features.auth.providers.*

interface TokenStorage {
    fun getSavedTokens(): BearerTokens?
    fun saveTokens(tokens: BearerTokens)
}

class MemoryTokenStorage : TokenStorage {
    var token: BearerTokens? = null
    override fun getSavedTokens(): BearerTokens? = token

    override fun saveTokens(tokens: BearerTokens) {
        token = tokens
    }

}