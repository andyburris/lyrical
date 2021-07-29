package room.game.answer

import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import client.SourcePlaylist
import client.totalPoints
import common.LyricalScaffold
import isRight
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp
import platform.*
import room.game.question.QuestionAppBar
import server.RoomState

@Composable
fun AnswerScreen(
    gameScreen: GameScreen.Answer,
    answeredQuestion: ClientGameQuestion.Answered,
    game: RoomState.Game.Client,
    modifier: Modifier = Modifier,
    onAnswerAction: (GameAction.Answer) -> Unit,
    onNavigateBack: () -> Unit
) {
    ProvidePalette(if (answeredQuestion.answer.isRight) LyricalTheme.colors.primaryPalette else LyricalTheme.colors.backgroundPalette) {
        LyricalScaffold(
            modifier = modifier.background(CurrentPalette.background).padding(32.dp),
            appBar = {
                QuestionAppBar(
                    questionIndex = gameScreen.questionIndex,
                    amountOfSongs = game.config.amountOfSongs,
                    sourcePlaylist = if (game.config.showSourcePlaylist) SourcePlaylist.Shown(answeredQuestion.track.sourcePlaylist) else SourcePlaylist.NotShown,
                    currentPoints = game.questions.totalPoints(),
                    onNavigateBack = onNavigateBack,
                )
            },
            title = {
                Column {
                    Text(
                        text = when(answeredQuestion.answer) {
                            is GameAnswer.Answered.Correct -> "Correct!"
                            is GameAnswer.Answered.Incorrect -> "Incorrect"
                            is GameAnswer.Answered.Skipped -> "Skipped"
                            is GameAnswer.Answered.Missed -> "Missed"
                        },
                        style = LyricalTheme.typography.h1,
                        color = CurrentPalette.contentPrimary,
                    )
                    if (answeredQuestion.answer.isRight) {
                        Text(
                            text = "+ ${(answeredQuestion.answer as GameAnswer.Answered.Correct).points} pts",
                            style = LyricalTheme.typography.subtitle1,
                            color = CurrentPalette.contentSecondary
                        )
                    }
                }
            },
            actionButton = {
                Button(onClick = { onAnswerAction.invoke(GameAction.Answer.NextScreen) }) { //TODO: change color based on whether answer is right
                    val hasNextQuestion = gameScreen.questionIndex < game.config.amountOfSongs - 1
                    Text(text = if (hasNextQuestion) "Next Question".uppercase() else "View Summary".uppercase())
                }
            },
            content = {
                Column { //TODO: Add Arrangement.SpacedBy(24.dp)
                    AnswerTrack(answeredQuestion.track, answeredQuestion.answer, modifier = Modifier.fillMaxWidth())
                    Leaderboard(game.leaderboard, gameScreen.questionIndex)
                }
            }
        )
    }
}