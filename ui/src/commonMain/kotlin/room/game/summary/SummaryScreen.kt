package room.game.summary

import GameAction
import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import client.totalPoints
import common.LyricalScaffold
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.ui.Modifier
import platform.Button
import platform.LyricalTheme
import platform.Text
import common.AppBar
import org.jetbrains.compose.common.ui.background
import room.game.answer.Leaderboard
import server.RoomState

@Composable
fun SummaryScreen(
    game: RoomState.Game.Client,
    modifier: Modifier = Modifier,
    onSummaryAction: (GameAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    LyricalScaffold(
        modifier = modifier.background(LyricalTheme.colors.primary),
        appBar = {
            AppBar(
                title = "Summary",
                onNavigateBack = onNavigateBack
            )
        },
        title = {
            Column {
                Text("Your Score".uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onPrimarySecondary)
                Text("${game.questions.totalPoints()}/${game.config.amountOfSongs} pts", style = LyricalTheme.typography.h1, color = LyricalTheme.colors.onPrimary) //TODO: add AnnotatedString with colors
            }
        },
        actionButton = {
            Button(onClick = {  }) {
                Text("Play Again")
            }
        },
        content = {
            Column { //TODO: add Arrangement.SpacedBy(48.dp)
                Leaderboard(game.leaderboard, game.config.amountOfSongs - 1)
                AnswerSummary(game.questions.map { it as? ClientGameQuestion.Answered ?: throw Error("All questions should be answered by the end screen") })
            }
        }
    )
}