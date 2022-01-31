package room.game

import GameAction
import GameScreen
import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import compose.multiplatform.ui.Modifier
import room.game.answer.AnswerScreen
import room.game.question.QuestionScreen
import room.game.summary.SummaryScreen
import server.RoomState

@Composable
fun GameRouter(
    game: RoomState.Game.Client,
    modifier: Modifier = Modifier,
    onGameAction: (GameAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    when (val screen = game.gameScreen) {
        is GameScreen.Question -> QuestionScreen(
            gameScreen = screen,
            question = game.questions[screen.questionIndex] as ClientGameQuestion.Unanswered,
            game = game,
            modifier = modifier,
            onQuestionAction = onGameAction,
            onNavigateBack = onNavigateBack
        )
        is GameScreen.Answer -> AnswerScreen(
            gameScreen = screen,
            answeredQuestion = game.questions[screen.questionIndex] as ClientGameQuestion.Answered,
            game = game,
            modifier = modifier,
            onAnswerAction = onGameAction,
            onNavigateBack = onNavigateBack
        )
        GameScreen.Summary -> SummaryScreen(
            game = game,
            modifier = modifier,
            onSummaryAction = onGameAction,
            onNavigateBack = onNavigateBack
        )
    }
}

