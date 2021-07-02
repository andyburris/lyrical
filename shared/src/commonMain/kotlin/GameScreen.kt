import kotlinx.serialization.Serializable

@Serializable
sealed class GameScreen {
    data class Question(val questionIndex: Int) : GameScreen()
    data class Answer(val questionIndex: Int) : GameScreen()
    object Summary : GameScreen()
}