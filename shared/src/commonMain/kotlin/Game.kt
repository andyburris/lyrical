import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val questions: List<GameQuestion>,
    val options: GameOptions
) {
    val currentQuestion = questions.firstOrNull { it.answer !is GameAnswer.Answered }
    val currentQuestionIndex = questions.indexOfFirst { it.answer !is GameAnswer.Answered }
    val lastQuestion = questions.lastOrNull { it.answer is GameAnswer.Answered }
    val lastQuestionIndex = questions.indexOfLast { it.answer is GameAnswer.Answered }

    val points get() = questions.map { it.answer }.filterIsInstance<GameAnswer.Answered.Correct>().sumOf { it.points }
    fun withNextAnswer(answer: UserAnswer): Game {
        val questionNumber = questions.indexOfFirst { it.answer is GameAnswer.Unanswered }
        return this.copy(questions = questions.replace(questionNumber) { it.withAnswer(answer) },)
    }
    fun withHintUsed(hint: GameHint): Game {
        val questionNumber = questions.indexOfFirst { it.answer is GameAnswer.Unanswered }
        return this.copy(questions = questions.replace(questionNumber) { it.withHintUsed(hint) })
    }
    val isEnded get() = questions.all { it.answer !is GameAnswer.Unanswered }
}

@Serializable
data class GameQuestion(
    val trackWithLyrics: TrackWithLyrics,
    val answer: GameAnswer = GameAnswer.Unanswered(emptyList()),
    val startingLineIndex: Int
) {
    val lyric = trackWithLyrics.lyricState.lyrics[startingLineIndex]
    val nextLyric = trackWithLyrics.lyricState.lyrics.getOrNull(startingLineIndex + 1) ?: "[End of Song]"
    val artist get() = trackWithLyrics.sourcedTrack.track.artists.first().name
    val playlist get() = trackWithLyrics.sourcedTrack.sourcePlaylist
    fun withAnswer(answer: UserAnswer): GameQuestion {
        return when (answer) {
            is UserAnswer.Answer -> {
                println("answered, formatted user answer = ${answer.answer.formatAnswer()}, formatted correct answer = ${trackWithLyrics.sourcedTrack.track.name.formatAnswer()}")
                val gameAnswer = when (answer.answer.formatAnswer() == trackWithLyrics.sourcedTrack.track.name.formatAnswer()) {
                    true -> GameAnswer.Answered.Correct(answer.hintsUsed)
                    false -> GameAnswer.Answered.Incorrect(answer.answer, answer.hintsUsed)
                }
                this.copy(answer = gameAnswer)
            }
            is UserAnswer.Skipped -> {
                val gameAnswer = GameAnswer.Answered.Skipped(answer.hintsUsed)
                this.copy(answer = gameAnswer)
            }
        }
    }
    fun withHintUsed(hint: GameHint): GameQuestion {
        return when(this.answer) {
            is GameAnswer.Answered -> throw Error("Can't add a hint to an answered question")
            is GameAnswer.Unanswered -> this.copy(answer = this.answer.copy(hintsUsed = this.answer.hintsUsed + hint))
        }
    }
}

@Serializable
enum class GameHint {
    Artist, NextLine
}

@Serializable
sealed class GameAnswer {
    abstract val hintsUsed: List<GameHint>
    @Serializable
    sealed class Answered() : GameAnswer() {

        @Serializable data class Correct(override val hintsUsed: List<GameHint>) : Answered() {
            val points = 1.0 - (if (GameHint.Artist in hintsUsed) 0.5 else 0.0) - (if (GameHint.NextLine in hintsUsed) 0.25 else 0.0)
        }
        @Serializable data class Incorrect(val answer: String, override val hintsUsed: List<GameHint>) : Answered()
        @Serializable data class Skipped(override val hintsUsed: List<GameHint>) : Answered()
    }
    @Serializable data class Unanswered(override val hintsUsed: List<GameHint>) : GameAnswer()
}
val GameAnswer.isRight get() = this is GameAnswer.Answered.Correct
val GameAnswer.isWrong get() = !this.isRight

sealed class UserAnswer {
    data class Answer(val answer: String, val hintsUsed: List<GameHint>) : UserAnswer()
    data class Skipped(val hintsUsed: List<GameHint>) : UserAnswer()
}

@Serializable
data class GameOptions(
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

fun List<TrackWithLyrics>.generateQuestions(config: GameOptions) = this.map { trackWithLyrics: TrackWithLyrics ->
        val randomLine: Int = trackWithLyrics.lyricState.lyrics.randomLyricIndex(config.difficulty, trackWithLyrics.sourcedTrack.track.name)
        GameQuestion(trackWithLyrics, startingLineIndex = randomLine)
    }