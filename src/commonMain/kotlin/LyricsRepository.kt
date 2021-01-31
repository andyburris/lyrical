import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class LyricsRepository(val httpClient: HttpClient = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}) {
    suspend fun getLyricsFor(tracks: List<SourcedTrack>): List<TrackWithLyrics> {
        println("getting lyrics for $tracks")
        //val url = "http://localhost:5050/lyrics"
        val url = "http://lyricalgame.herokuapp.com/lyrics"
        val responses = httpClient.get<List<LyricResponse>>(url) {
            header("lyricRequests", tracks.map { LyricRequest(it.track.name, it.track.artists.map { it.name }, it.track.uri.uri) }.encodeToString())
        }
        return responses.mapNotNull { lyricResponse ->
            if (lyricResponse.lyrics == null) return@mapNotNull null
            TrackWithLyrics(tracks.first { it.track.uri.uri == lyricResponse.trackURI }, LyricsState.Available(lyricResponse.lyrics))
        }
    }
}

@Serializable
data class LyricRequest(val name: String, val artists: List<String>, val trackID: String)

@Serializable
data class LyricResponse(val trackURI: String, val lyrics: List<String>?)

fun List<LyricRequest>.encodeToString() = Json.Default.encodeToString(ListSerializer(LyricRequest.serializer()), this)
fun String.decodeLyricRequestsFromString(): List<LyricRequest> = Json.Default.decodeFromString(ListSerializer(LyricRequest.serializer()), this)