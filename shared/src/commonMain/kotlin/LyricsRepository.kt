import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

data class LyricsRepository(val httpClient: HttpClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}, val debug: Boolean = false) {
    suspend fun getLyricsFor(tracks: List<SourcedTrack>): List<TrackWithLyrics> {
        println("getting lyrics for $tracks")
        val url = when(debug) {
            false -> "https://lyricalgame.herokuapp.com/lyrics"
            true -> "http://localhost:5050/lyrics"
        }
        val responses = httpClient.get(url) {
            val lyricRequests = tracks.map { LyricRequest(it.track.name.filterHeader(), it.track.artists.map { it.name.filterHeader() }, it.track.uri.uri) }
            println("lyricRequests = $lyricRequests")
            header("lyricRequests", lyricRequests.encodeToString())
        }.body<List<LyricResponse>>()
        return responses.mapNotNull { lyricResponse ->
            if (lyricResponse.lyrics == null) return@mapNotNull null
            TrackWithLyrics(tracks.first { it.track.uri.uri == lyricResponse.trackURI }, LyricsState.Available(lyricResponse.lyrics.filter { !it.startsWith('[') }))
        }
    }

    /**
     * Filters song names to only characters that can be sent in a header
     */
    private fun String.filterHeader() = this.filter { it in '0'..'9' || it in 'a'..'z' || it in 'A'..'Z' || it in listOf(' ', '(', ')', '!', '#', '$', '%', '&', '*', '+', '-', '.', '^', '`', '|', '~') }
}

@Serializable
data class LyricRequest(val name: String, val artists: List<String>, val trackID: String)

@Serializable
data class LyricResponse(val trackURI: String, val lyrics: List<String>?)

fun List<LyricRequest>.encodeToString() = Json.Default.encodeToString(ListSerializer(LyricRequest.serializer()), this)
fun String.decodeLyricRequestsFromString(): List<LyricRequest> = Json.Default.decodeFromString(ListSerializer(LyricRequest.serializer()), this)