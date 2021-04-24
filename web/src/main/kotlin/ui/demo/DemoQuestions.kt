package ui.demo

import GameConfig
import GameQuestion
import SourcedTrack
import TrackWithLyrics
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.models.Track
import generateQuestions
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.browser.window
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

suspend fun getDemoQuestions(genre: DemoGenre): List<GameQuestion> {
    println("getting ${genre.urlString()} demo questions")
    val http = window.location.href.takeWhile { it != ':' }
    val url = "$http://${window.location.host}/assets/demo/${genre.urlString()}.json"
    return Json.Default.decodeFromString(PlaylistExport.serializer(), HttpClient().get<String>(url)).tracks.shuffled().take(10).generateQuestions(GameConfig())
}

@Serializable
data class PlaylistExport(val name: String, val tracks: List<TrackWithLyrics>)
