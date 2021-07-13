package ui.room.game

import GameAction
import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import server.RoomState

@Composable
fun GameRouter(game: RoomState.Game.Client, onGameAction: (GameAction) -> Unit){
    when(val screen = game.gameScreen) {
        is GameScreen.Question -> QuestionScreen(
            gameScreen = screen,
            question = game.questions[screen.questionIndex] as ClientGameQuestion.Unanswered,
            game = game,
            onQuestionAction = onGameAction
        )
        is GameScreen.Answer -> AnswerScreen(
            gameScreen = screen,
            answeredQuestion = game.questions[screen.questionIndex] as ClientGameQuestion.Answered,
            game = game,
            onAnswerAction = onGameAction
        )
        GameScreen.Summary -> SummaryScreen(
            game = game,
            onSummaryAction = onGameAction
        )
    }
}