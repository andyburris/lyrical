package room.game.answer

import GameAnswer
import GenericTrack
import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.Image
import compose.multiplatform.foundation.Resource
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import isWrong
import model.GenericGameTrack
import org.jetbrains.compose.common.ui.*
import org.jetbrains.compose.common.ui.draw.clip
import platform.*

@Composable
fun AnswerTrack(
    gameTrack: GenericGameTrack,
    answer: GameAnswer.Answered,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(LyricalTheme.shapes.large)
            .background(CurrentPalette.backgroundDark)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (answer is GameAnswer.Answered.Incorrect) {
            YourAnswer(answer)
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(Resource.Url(track.album.imageURL), contentDescription = null, modifier = Modifier.size(48.dp))
        Column {
            Text(track.name, style = LyricalTheme.typography.h2, color = CurrentPalette.contentPrimary)
            Text(track.artists.joinToString { it.name }, style = LyricalTheme.typography.h2, color = CurrentPalette.contentSecondary)
        }
    }
}