import kotlinx.serialization.Serializable
import server.ServerGameQuestion

@Serializable
sealed class UserAnswer {
    @Serializable
    data class Answer(val answer: String) : UserAnswer()
    @Serializable
    object Skipped : UserAnswer()
}


private val answerFormatRegex = Regex("( \\(.*\\))+|( -.*)|( \\[.*\\])|(([( ])feat.*)|(([( ])ft.*)")

fun String.formatAnswer(): String = this
    .lowercase()
    .replace(answerFormatRegex, "")
    .stripAccents()
    .replace("&", "and")
    .filter { it in 'a'..'z' || it in '0'..'9'}