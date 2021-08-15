package room.game.answer

import GameAnswer
import GenericTrack
import androidx.compose.runtime.Composable
import isWrong
import model.GenericGameTrack
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.size
import org.jetbrains.compose.common.ui.unit.dp
import platform.CurrentPalette
import platform.Image
import platform.LyricalTheme
import platform.Text

@Composable
fun AnswerTrack(
    gameTrack: GenericGameTrack,
    answer: GameAnswer.Answered,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            //.clip(LyricalTheme.shapes.medium) TODO: readd once shapes are supported
            .background(CurrentPalette.backgroundDark)
            .padding(24.dp)
    ) {
        if (answer is GameAnswer.Answered.Incorrect) {
            YourAnswer(answer)
        }
        Column { //TODO: Add Arrangement.SpacedBy(8.dp)
            if (answer.isWrong) {
                Text("Correct Answer".uppercase(), style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary)
            }
            SongItem(gameTrack.track)
        }
    }
}

@Composable
private fun YourAnswer(
    answer: GameAnswer.Answered.Incorrect,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text("Your Answer".uppercase(), style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary)
        Text(answer.answer, style = LyricalTheme.typography.h2, color = CurrentPalette.contentPrimary)
    }
}

@Composable
private fun SongItem(track: GenericTrack, modifier: Modifier = Modifier) {
    Row(modifier) { //TODO: add Arrangement.SpacedBy(16.dp)
        Image(track.album.imageURL, contentDescription = null, modifier = Modifier.size(48.dp))
        Column {
            Text(track.name, style = LyricalTheme.typography.h2, color = CurrentPalette.contentPrimary)
            Text(track.artists.joinToString { it.name }, style = LyricalTheme.typography.h2, color = CurrentPalette.contentSecondary)
        }
    }
}