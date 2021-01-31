import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

data class Game(val questions: List<GameQuestion>, val config: GameConfig) {
    val points get() = questions.map { it.answer }.filterIsInstance<GameAnswer.Correct>().sumByDouble { it.points }
    fun withNextAnswer(answer: UserAnswer): Game {
        val questionNumber = questions.indexOfFirst { it.answer is GameAnswer.Unanswered }
        return this.copy(
            questions = questions.replace(questionNumber) { it.withAnswer(answer) }
        )
    }
    val isEnded get() = questions.all { it.answer !is GameAnswer.Unanswered }
}

data class GameQuestion(val trackWithLyrics: TrackWithLyrics, val answer: GameAnswer = GameAnswer.Unanswered) {
    fun withAnswer(answer: UserAnswer): GameQuestion {
        return when (answer) {
            is UserAnswer.Answer -> {
                val gameAnswer = when (answer.answer.formatAnswer() == trackWithLyrics.sourcedTrack.track.name.formatAnswer()) {
                    true -> GameAnswer.Correct(answer.potentialPoints)
                    false -> GameAnswer.Incorrect(answer.answer)
                }
                this.copy(answer = gameAnswer)
            }
            UserAnswer.Skipped -> {
                this.copy(answer = GameAnswer.Skipped)
            }
        }
    }
}

sealed class GameAnswer {
    data class Correct(val points: Double) : GameAnswer()
    data class Incorrect(val answer: String) : GameAnswer()
    object Skipped : GameAnswer()
    object Unanswered : GameAnswer()
}

sealed class UserAnswer {
    data class Answer(val answer: String, val potentialPoints: Double) : UserAnswer()
    object Skipped : UserAnswer()
}

@Serializable
@OptIn(ExperimentalTime::class)
data class GameConfig(
    val amountOfSongs: Int = 10,
    val timer: Int = 0,
    val showSourcePlaylist: Boolean = false,
    val distributePlaylistsEvenly: Boolean = true
)

/*@OptIn(ExperimentalTime::class)
object DurationAsDoubleSerializer : KSerializer<Duration> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Duration", PrimitiveKind.DOUBLE)
    override fun deserialize(decoder: Decoder): Duration = decoder.decodeDouble().milliseconds
    override fun serialize(encoder: Encoder, value: Duration) = encoder.encodeDouble(value.inMilliseconds)
}*/

private val answerFormatRegex = Regex("( \\(.*\\))+|( -.*)|( \\[.*])|(feat.*)|(ft.*)")

fun String.formatAnswer(): String = this.toLowerCase().replace(answerFormatRegex, "").filter { it in 'a'..'z' }

data class SourcedTrack(val track: Track, val sourcePlaylist: SimplePlaylist)

@Serializable
sealed class LyricsState {
    @Serializable object NotAvailable : LyricsState()
    @Serializable data class Available(val lyrics: List<String>) : LyricsState()
}

data class TrackWithLyrics(val sourcedTrack: SourcedTrack, val lyricState: LyricsState.Available)

fun List<TrackWithLyrics>.toQuestions() = this.map { GameQuestion(it) }