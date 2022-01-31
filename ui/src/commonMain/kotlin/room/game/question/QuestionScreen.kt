package room.game.question

import GameAction
import GameScreen
import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import client.totalPoints
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.foundation.modifier.verticalScroll
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import platform.LyricalTheme
import server.RoomState


@Composable
fun QuestionScreen(
    gameScreen: GameScreen.Question,
    question: ClientGameQuestion.Unanswered,
    game: RoomState.Game.Client,
    modifier: Modifier = Modifier,
    onQuestionAction: (GameAction.Question) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(modifier.verticalScroll()) {
        Column(
            //TODO: add Arrangement.SpacedBy(24.dp)
            modifier = Modifier
                .background(LyricalTheme.colors.backgroundDark)
                .padding(32.dp)
        ) {
            QuestionAppBar(
                questionIndex = gameScreen.questionIndex,
                amountOfSongs = game.config.amountOfSongs,
                sourcePlaylist = question.sourcePlaylist,
                currentPoints = game.questions.totalPoints(),
                onNavigateBack = onNavigateBack
            )
            LyricSection(question, gameScreen.questionIndex, onQuestionAction = onQuestionAction)
        }
        Column(Modifier.background(LyricalTheme.colors.background).padding(32.dp)) {
            AnswerSection(onAnswer = { onQuestionAction.invoke(GameAction.Question.AnswerQuestion(gameScreen.questionIndex, it)) })
        }
    }
}

