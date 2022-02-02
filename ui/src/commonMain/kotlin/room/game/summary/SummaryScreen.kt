package room.game.summary

import GameAction
import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import client.totalPoints
import common.AppBar
import common.LyricalScaffold
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.foundation.modifier.verticalScroll
import compose.multiplatform.foundation.modifiers.fillMaxSize
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import platform.*
import room.game.answer.Leaderboard
import server.RoomState

@Composable
fun SummaryScreen(
    game: RoomState.Game.Client,
    modifier: Modifier = Modifier,
    onSummaryAction: (GameAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    ProvidePalette(LyricalTheme.colors.primaryPalette) {
        LyricalScaffold(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll()
                .background(CurrentPalette.background)
                .padding(32.dp),
            appBar = {
                AppBar(
                    title = "Summary",
                    onNavigateBack = onNavigateBack
                )
            },
            title = {
                Column {
                    Text("Your Score".uppercase(), style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary)
                    Text("${game.questions.totalPoints()}/${game.config.amountOfSongs} pts", style = LyricalTheme.typography.h1, color = CurrentPalette.contentPrimary) //TODO: add AnnotatedString with colors
                }
            },
            actionButton = {
                Button(onClick = {  }) {
                    Text("Play Again")
                }
            },
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Leaderboard(game.leaderboard, game.config.amountOfSongs - 1)
                    AnswerSummary(game.questions.map { it as? ClientGameQuestion.Answered ?: throw Error("All questions should be answered by the end screen") })
                }
            }
        )
    }
}