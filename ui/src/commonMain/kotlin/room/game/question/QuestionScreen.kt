package room.game.question

import GameAction
import GameScreen
import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import client.totalPoints
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp
import platform.LyricalTheme
import platform.verticalScroll
import server.RoomState


@OptIn(ExperimentalComposeWebWidgetsApi::class)
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

