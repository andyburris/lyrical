package room.game.answer

import androidx.compose.runtime.Composable
import client.Leaderboard
import client.LeaderboardPlayer
import common.Icon
import jetbrains.compose.common.shapes.CircleShape
import name
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Alignment
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.size
import org.jetbrains.compose.common.ui.unit.dp
import platform.CurrentPalette
import platform.LyricalTheme
import platform.Text

@Composable
fun Leaderboard(
    leaderboard: Leaderboard,
    currentQuestionIndex: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier) { //TODO: add Arrangement.SpacedBy(16.dp)
        Text("Leaderboard".uppercase(), style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary)
        Column { //TODO: add Arrangement.SpacedBy(12.dp)
            leaderboard.players.sortedBy { it.points }.forEachIndexed { index, leaderboardPlayer ->
                LeaderboardPlayerItem(player = leaderboardPlayer, position = index, answeredCurrentQuestion = leaderboardPlayer.questionsAnswered == currentQuestionIndex + 1)
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
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { //TODO: add Arrangement.SpacedBy(16.dp)
        val contentColor = if (answeredCurrentQuestion) CurrentPalette.contentPrimary else CurrentPalette.contentSecondary
        ProfilePictureBox(
            modifier = Modifier
                .clip(CircleShape)
                .background(CurrentPalette.backgroundDark)
                .size(32.dp)
        ) {
            Text((position + 1).toString(), style = LyricalTheme.typography.subtitle1, color = contentColor)
        }

        Text(player.user.name, style = LyricalTheme.typography.subtitle1, color = contentColor) //TODO: add Modifier.weight(1f)
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

        //TODO: add loading/answered icon
    }
}


@Composable
fun ProfilePictureBox(modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier, content = content)
}
