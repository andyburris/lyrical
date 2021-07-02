import client.ClientGameQuestion
import kotlinx.serialization.Serializable

@Serializable
sealed class GameAnswer {
    abstract val hintsUsed: List<Hint>
    @Serializable sealed class Answered() : GameAnswer() {
        @Serializable data class Correct(override val hintsUsed: List<Hint>) : Answered() {
            val points = 1.0 - (if (Hint.Artist in hintsUsed) 0.5 else 0.0) - (if (Hint.NextLyric in hintsUsed) 0.25 else 0.0)
        }
        @Serializable data class Incorrect(val answer: String, override val hintsUsed: List<Hint>) : Answered()
        @Serializable data class Skipped(override val hintsUsed: List<Hint>) : Answered()
        @Serializable data class Missed(override val hintsUsed: List<Hint>) : Answered()
    }
    data class Unanswered(override val hintsUsed: List<Hint>) : GameAnswer()
}
val GameAnswer.Answered.isRight get() = this is GameAnswer.Answered.Correct
val GameAnswer.Answered.isWrong get() = !this.isRight
val GameAnswer.points get() = if (this !is GameAnswer.Answered.Correct) 0.0 else this.points

fun List<GameAnswer>.withAnswer(questionNumber: Int, answer: GameAnswer) = this.mapIndexed { index, oldAnswer -> if (index == questionNumber) answer else oldAnswer }
fun List<GameAnswer>.totalPoints() = sumOf { it.points }
fun List<GameAnswer>.answeredQuestions() = count { it is GameAnswer.Answered }