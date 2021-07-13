package room.game.summary

import GameAction
import androidx.compose.runtime.Composable
import client.totalPoints
import common.LyricalScaffold
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.ui.Modifier
import platform.Button
import platform.LyricalTheme
import platform.Text
import common.AppBar
import room.game.answer.Leaderboard
import server.RoomState

@Composable
fun SummaryScreen(
    game: RoomState.Game.Client,
    modifier: Modifier = Modifier,
    onSummaryAction: (GameAction) -> Unit
) {
    LyricalScaffold(
        modifier = modifier,
        appBar = {
            AppBar("Summary")
        },
        title = {
            Column {
                Text("Your Score".uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onPrimarySecondary)
                Text("${game.questions.totalPoints()}/${game.config.amountOfSongs} pts") //TODO: add AnnotatedString with colors
            }
        },
        actionButton = {
            Button(onClick = {}) {
                Text("Play Again")
            }
        },
        content = {
            Column { //TODO: add Arrangement.SpacedBy(48.dp)
                Leaderboard(game.leaderboard, game.config.amountOfSongs - 1)

            }
        }
    )
}