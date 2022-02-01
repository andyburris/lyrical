package room.game.answer

import GameAction
import GameAnswer
import GameScreen
import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import client.SourcePlaylist
import client.totalPoints
import common.LyricalScaffold
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.foundation.modifier.verticalScroll
import compose.multiplatform.foundation.modifiers.fillMaxSize
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import isRight
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
            modifier = modifier
                .fillMaxSize()
                .verticalScroll()
                .background(CurrentPalette.background)
                .padding(32.dp),
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
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    AnswerTrack(answeredQuestion.track, answeredQuestion.answer, modifier = Modifier.fillMaxWidth())
                    Leaderboard(game.leaderboard, gameScreen.questionIndex)
                }
            }
        )
    }
}