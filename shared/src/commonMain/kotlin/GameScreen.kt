import kotlinx.serialization.Serializable

@Serializable
sealed class GameScreen {
    @Serializable data class Question(val questionIndex: Int) : GameScreen()
    @Serializable data class Answer(val questionIndex: Int) : GameScreen()
    @Serializable object Summary : GameScreen()
}