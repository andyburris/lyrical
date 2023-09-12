import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import java.util.NoSuchElementException

private val invalidSearch = Regex("( \\(.*\\))+|( \\[.*])|(-.*)|([^\\w\\d\\s&])")
fun String.sanitizeForSearch() = this.replace(invalidSearch, "")
fun String.sanitizeForCompare() = this.sanitizeForSearch().replace(" ", "").toLowerCase()
internal val unnecessaryScrapingRegex = Regex("(<section[^>]*>|</section[^>]*>)+|(<![^>]*>)+|(<p[^>]*>|</p[^>]*>)+|(<a[^>]*>|</a[^>]*>)+|(<span[^>]*>|</span[^>]*>)+|(<i>|</i>)+|(<b>|</b>)+|(<div[^>]*>|</div[^>]*>)+")

data class GeniusRepository(
    val apiKey: String,
    val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
) {

    suspend fun getLyrics(trackName: String, artists: List<String>): String? {
        val songURL = getSongURL(trackName, artists) ?: return null
        return scrapeLyrics(songURL)
    }

    private suspend fun getSongURL(trackName: String, artists: List<String>): String? {
        val url = artists.firstNotNullOfOrNull {
            getSongURLByArtist(trackName, it, artists)
        }
        if (url == null) Error("Genius contains no results for $trackName by ${artists.joinToString()}").printStackTrace()
        return url
    }

    private suspend fun getSongURLByArtist(trackName: String, artist: String, allArtists: List<String>): String? {
        val searchTerm = "${trackName.sanitizeForSearch()} ${artist.sanitizeForSearch()}".replace(" ", "%20")
        val url = "https://api.genius.com/search?q=$searchTerm"
        return try {
            val response = httpClient.get(url) {
                bearerAuth(apiKey)
            }.body<JsonObject>()
//            println(response)
            val bestHit = response.hits().findBestHitUrl(trackName, allArtists)
            bestHit?.hitUrl()
        } catch (e: NoSuchElementException) {
//            Error("Genius contains no results for $trackName by $artist (search term was ${trackName.replace(invalidSearch, "")} $artist")
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

private fun JsonElement.stringifyHit() = "${this.hitTitle()} - ${this.hitArtist()}"
private fun JsonArray.findBestHitUrl(trackName: String, allArtists: List<String>): JsonElement? =
    this
        .take(5)
        .firstOrNull { allArtists.matchExactArtist(it.hitArtist()) }
    ?: this
        .take(5)
        .firstOrNull { allArtists.matchArtistAnywhere(it.hitArtist()) }
    ?: this
        .firstOrNull { allArtists.matchExactArtist(it.hitArtist()) }
    ?: this
        .firstOrNull { allArtists.matchArtistAnywhere(it.hitArtist()) }

private fun List<String>.matchExactArtist(artistToMatch: String) = artistToMatch.sanitizeForCompare() in this.map { it.sanitizeForCompare() }
private fun List<String>.matchArtistAnywhere(artistToMatch: String) = this.any { it.sanitizeForCompare() in artistToMatch.sanitizeForCompare() }

private fun JsonObject.hits(): JsonArray = this["response"]!!
    .jsonObject["hits"]!!.jsonArray

private fun JsonElement.hitTitle() =
    jsonObject["result"]!!
        .jsonObject["title"]!!
        .jsonPrimitive.content

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
