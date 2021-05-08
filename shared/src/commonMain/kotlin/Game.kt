import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

data class Game(val questions: List<GameQuestion>, val config: GameConfig) {
    val points get() = questions.map { it.answer }.filterIsInstance<GameAnswer.Answered.Correct>().sumByDouble { it.points }
    fun withNextAnswer(answer: UserAnswer): Game {
        val questionNumber = questions.indexOfFirst { it.answer is GameAnswer.Unanswered }
        return this.copy(
            questions = questions.replace(questionNumber) { it.withAnswer(answer) }
        )
    }
    val isEnded get() = questions.all { it.answer !is GameAnswer.Unanswered }
}

data class GameQuestion(val trackWithLyrics: TrackWithLyrics, val answer: GameAnswer = GameAnswer.Unanswered, val startingLineIndex: Int) {
    val lyric = trackWithLyrics.lyricState.lyrics[startingLineIndex]
    val nextLyric = trackWithLyrics.lyricState.lyrics.getOrNull(startingLineIndex + 1) ?: "[End of Song]"
    val artist get() = trackWithLyrics.sourcedTrack.track.artists.first().name
    val playlist get() = trackWithLyrics.sourcedTrack.sourcePlaylist
    fun withAnswer(answer: UserAnswer): GameQuestion {
        return when (answer) {
            is UserAnswer.Answer -> {
                println("answered, formatted user answer = ${answer.answer.formatAnswer()}, formatted correct answer = ${trackWithLyrics.sourcedTrack.track.name.formatAnswer()}")
                val gameAnswer = when (answer.answer.formatAnswer() == trackWithLyrics.sourcedTrack.track.name.formatAnswer()) {
                    true -> GameAnswer.Answered.Correct(answer.withNextLine, answer.withArtist)
                    false -> GameAnswer.Answered.Incorrect(answer.answer, answer.withNextLine, answer.withArtist)
                }
                this.copy(answer = gameAnswer)
            }
            is UserAnswer.Skipped -> {
                this.copy(answer = GameAnswer.Answered.Skipped(answer.withNextLine, answer.withArtist))
            }
        }
    }
}

sealed class GameAnswer {
    sealed class Answered() : GameAnswer() {
        abstract val withNextLine: Boolean
        abstract val withArtist: Boolean
        data class Correct(override val withNextLine: Boolean, override val withArtist: Boolean) : Answered() {
            val points = 1.0 - (if (withArtist) 0.5 else 0.0) - (if (withNextLine) 0.25 else 0.0)
        }
        data class Incorrect(val answer: String, override val withNextLine: Boolean, override val withArtist: Boolean) : Answered()
        data class Skipped(override val withNextLine: Boolean, override val withArtist: Boolean) : Answered()
    }
    object Unanswered : GameAnswer()
}
val GameAnswer.isRight get() = this is GameAnswer.Answered.Correct
val GameAnswer.isWrong get() = !this.isRight

sealed class UserAnswer {
    data class Answer(val answer: String, val withNextLine: Boolean, val withArtist: Boolean) : UserAnswer()
    data class Skipped(val withNextLine: Boolean, val withArtist: Boolean) : UserAnswer()
}

@Serializable
@OptIn(ExperimentalTime::class)
data class GameConfig(
    val amountOfSongs: Int = 10,
    val timer: Int = 0,
    val showSourcePlaylist: Boolean = false,
    val distributePlaylistsEvenly: Boolean = true,
    val difficulty: Difficulty = Difficulty.Medium
)

@Serializable enum class Difficulty { Easy, Medium, Hard }

/*@OptIn(ExperimentalTime::class)
object DurationAsDoubleSerializer : KSerializer<Duration> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Duration", PrimitiveKind.DOUBLE)
    override fun deserialize(decoder: Decoder): Duration = decoder.decodeDouble().milliseconds
    override fun serialize(encoder: Encoder, value: Duration) = encoder.encodeDouble(value.inMilliseconds)
}*/

private val answerFormatRegex = Regex("( \\(.*\\))+|( -.*)|( \\[.*\\])|(([( ])feat.*)|(([( ])ft.*)")

fun String.formatAnswer(): String = this
    .toLowerCase()
    .replace(answerFormatRegex, "")
    .stripAccents()
    .replace("&", "and")
    .filter { it in 'a'..'z' || it in '0'..'9'}

@Serializable
data class SourcedTrack(val track: Track, val sourcePlaylist: SimplePlaylist)

@Serializable
sealed class LyricsState {
    @Serializable object NotAvailable : LyricsState()
    @Serializable data class Available(val lyrics: List<String>) : LyricsState()
}

@Serializable
data class TrackWithLyrics(val sourcedTrack: SourcedTrack, val lyricState: LyricsState.Available)

fun List<TrackWithLyrics>.generateQuestions(config: GameConfig) = this.map { trackWithLyrics: TrackWithLyrics ->
        val randomLine: Int = trackWithLyrics.lyricState.lyrics.randomLyricIndex(config.difficulty, trackWithLyrics.sourcedTrack.track.name)
        GameQuestion(trackWithLyrics, startingLineIndex = randomLine)
    }