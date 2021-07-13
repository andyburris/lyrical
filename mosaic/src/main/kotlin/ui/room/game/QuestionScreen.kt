package ui.room.game

import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import server.RoomState

@Composable
fun QuestionScreen(
    gameScreen: GameScreen.Question,
    question: ClientGameQuestion.Unanswered,
    game: RoomState.Game.Client,
    onQuestionAction: (GameAction.Question) -> Unit
) {

}