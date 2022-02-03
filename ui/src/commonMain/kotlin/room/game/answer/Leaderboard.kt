package room.game.answer

import androidx.compose.runtime.Composable
import client.Leaderboard
import client.LeaderboardPlayer
import common.Icon
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Box
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.foundation.modifier.BoxScope
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.unit.dp
import name
import org.jetbrains.compose.common.ui.*
import org.jetbrains.compose.common.ui.draw.clip
import platform.CurrentPalette
import platform.LyricalTheme

@Composable
fun Leaderboard(
    leaderboard: Leaderboard,
    currentQuestionIndex: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Leaderboard".uppercase(), style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary)
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            leaderboard.players.sortedBy { it.points }.forEachIndexed { index, leaderboardPlayer ->
                LeaderboardPlayerItem(
                    player = leaderboardPlayer,
                    position = index,
                    answeredCurrentQuestion = leaderboardPlayer.questionsAnswered == currentQuestionIndex + 1
                )
            }
        }
    }
}

@Composable
private fun LeaderboardPlayerItem(
    player: LeaderboardPlayer,
    position: Int,
    answeredCurrentQuestion: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val contentColor = if (answeredCurrentQuestion) CurrentPalette.contentPrimary else CurrentPalette.contentSecondary
        ProfilePictureBox(
            modifier = Modifier
                .clip(CircleShape)
                .background(CurrentPalette.backgroundDark)
                .size(32.dp)
        ) {
            Text((position + 1).toString(), style = LyricalTheme.typography.subtitle1, color = contentColor) //TODO: add Modifier.align(Alignment.Center)
        }

        Text(
            text = player.user.name,
            style = LyricalTheme.typography.subtitle1,
            color = contentColor,
            modifier = Modifier.weight(1f)
        )
        Text("${player.points} pts", style = LyricalTheme.typography.subtitle1, color = contentColor)
        when(answeredCurrentQuestion) {
            true -> Icon(
                icon = Icon.CheckCircle,
                contentDescription = "Answered"
            )
            false -> Icon(
                icon = Icon.Hourglass,
                contentDescription = "Not answered yet",
                tint = CurrentPalette.contentSecondary
            )
        }
    }
}


@Composable
fun ProfilePictureBox(modifier: Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(modifier, content = content)
}
