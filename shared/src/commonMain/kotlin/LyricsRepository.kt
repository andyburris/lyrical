import com.adamratzman.spotify.utils.Language
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import model.GenericGameTrack
import model.GenericPlaylist

data class LyricsRepository(val httpClient: HttpClient = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}, val debug: Boolean = false) {
    suspend fun getLyricsFor(tracks: List<Pair<GenericTrack, GenericPlaylist>>): List<GenericGameTrack> {
        println("getting lyrics for $tracks")
        val url = "$serverURL/lyrics"
        val responses = httpClient.get<List<LyricResponse>>(url) {
            val lyricRequests = tracks.map { track -> LyricRequest(track.first.name.filterHeader(), track.first.artists.map { it.name.filterHeader() }, track.first.id) }
            println("lyricRequests = $lyricRequests")
            header("lyricRequests", lyricRequests.encodeToString())
        }
        return responses.mapNotNull { lyricResponse ->
            if (lyricResponse.lyrics == null) return@mapNotNull null
            val (track, playlist) = tracks.first { it.first.id == lyricResponse.trackURI }
            GenericGameTrack(track, playlist, lyricResponse.lyrics.filter { !it.startsWith('[') })
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