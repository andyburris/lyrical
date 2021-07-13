package ui.room.game

import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import server.RoomState

@Composable
fun AnswerScreen(
    gameScreen: GameScreen.Answer,
    answeredQuestion: ClientGameQuestion.Answered,
    game: RoomState.Game.Client,
    onAnswerAction: (GameAction.Answer) -> Unit
) {

}