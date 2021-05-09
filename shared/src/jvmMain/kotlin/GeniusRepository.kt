import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.*

data class GeniusRepository(
    val apiKey: String,
    val httpClient: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
) {
    private val invalidSearch = Regex("( \\(.*\\))+|( \\[.*])|(-.*)|([^\\w\\d\\s&])")
    internal val unnecessaryScrapingRegex = Regex("(<section[^>]*>|</section[^>]*>)+|(<![^>]*>)+|(<p[^>]*>|</p[^>]*>)+|(<a[^>]*>|</a[^>]*>)+|(<span[^>]*>|</span[^>]*>)+|(<i>|</i>)+|(<b>|</b>)+|(<div[^>]*>|</div[^>]*>)+")

    suspend fun getLyrics(trackName: String, artists: List<String>): String? {
        val songURL = getSongURL(trackName, artists) ?: return null
        return scrapeLyrics(songURL)
    }

    private suspend fun getSongURL(trackName: String, artists: List<String>): String? {
        artists.forEach {
            val url = getSongURLByArtist(trackName, it, artists)
            if (url != null) return url
        }
        return null
    }

    private suspend fun getSongURLByArtist(trackName: String, artist: String, allArtists: List<String>): String? {
        val url = "https://api.genius.com/search?q=${trackName.replace(invalidSearch, "")} ${artist}&access_token=$apiKey"
        val response = httpClient.get<JsonObject>(url) {}
        //println(response)
        return try {
            response.hits().first { hit -> allArtists.any { it.replace(invalidSearch, "").replace(" ", "").toLowerCase() in hit.hitArtist().replace(invalidSearch, "").replace(" ", "").toLowerCase() } }.hitUrl()
        } catch (e: Exception) {
            Error("Genius contains no results for $trackName by $artist (search term was ${trackName.replace(invalidSearch, "")} $artist").printStackTrace()
            null
        }
    }
}
private fun JsonObject.hits() = this["response"]!!
    .jsonObject["hits"]!!.jsonArray

private fun JsonElement.hitArtist() =
    jsonObject["result"]!!
    .jsonObject["primary_artist"]!!
    .jsonObject["name"]!!
    .jsonPrimitive.content

private fun JsonElement.hitUrl() =
    jsonObject["result"]!!
        .jsonObject["url"]!!
        .jsonPrimitive.content

expect suspend fun GeniusRepository.scrapeLyrics(songURL: String): String?
