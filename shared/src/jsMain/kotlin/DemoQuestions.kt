import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.browser.window
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun getDemoQuestions(genre: DemoGenre): List<GameQuestion> {
    println("getting ${genre.urlString()} demo questions")
    val http = window.location.href.takeWhile { it != ':' }
    val url = "$http://${window.location.host}/assets/demo/${genre.urlString()}.json"
    return Json.Default.decodeFromString(PlaylistExport.serializer(), HttpClient().get<String>(url)).tracks.shuffled().take(10).generateQuestions(GameConfig())
}

@Serializable
data class PlaylistExport(val name: String, val tracks: List<TrackWithLyrics>)

sealed class DemoGenre {
    sealed class Rock : DemoGenre() {
        object AltRock : Rock()
    }
    sealed class Pop : DemoGenre() {
        object Modern : Pop()
    }
    sealed class Rap : DemoGenre() {
        object Modern : Rap()
    }
}

fun String.toDemoGenre() = when(this.toLowerCase()) {
    "altrock" -> DemoGenre.Rock.AltRock
    "pop" -> DemoGenre.Pop.Modern
    "rap" -> DemoGenre.Rap.Modern
    else -> throw Error("\"$this\" is not a properly formatted genre")
}

fun DemoGenre.urlString() = when(this) {
    DemoGenre.Rock.AltRock -> "altrock"
    DemoGenre.Pop.Modern -> "pop"
    DemoGenre.Rap.Modern -> "rap"
}