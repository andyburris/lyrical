import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.spotifyAppApi
import kotlin.time.Duration
import kotlin.time.ExperimentalTime



private val answerFormatRegex = Regex("( \\(.*\\))+|(-.*)|( \\[.*])")
fun String.formatAnswer(): String = this.toLowerCase().replace(answerFormatRegex, "").filter { it in 'a'..'z' }

@OptIn(ExperimentalTime::class)
data class GameConfig(
    val amountOfSongs: Int = 10,
    val timer: Duration? = null,
    val showSourcePlaylist: Boolean = false,
    val distributePlaylistsEvenly: Boolean = true
)
data class TrackWithLyrics(val track: Track, val lyrics: List<String>, val sourcePlaylistURI: String)
data class GameQuestion(val trackWithLyrics: TrackWithLyrics, val answer: GameAnswer = GameAnswer.Unanswered)
sealed class GameAnswer {
    data class Correct(val points: Double) : GameAnswer()
    data class Incorrect(val answer: String) : GameAnswer()
    object Skipped : GameAnswer()
    object Unanswered : GameAnswer()
}

data class Game(val questions: List<GameQuestion>, val config: GameConfig, val sourcePlaylists: List<SimplePlaylist>) {
    val points get() = questions.map { it.answer }.filterIsInstance<GameAnswer.Correct>().sumByDouble { it.points }
}


fun List<TrackWithLyrics>.toQuestions() = this.map { GameQuestion(it) }