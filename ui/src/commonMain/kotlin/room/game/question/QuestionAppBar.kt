package room.game.question

import androidx.compose.runtime.Composable
import client.SourcePlaylist
import common.AppBar
import compose.multiplatform.foundation.Text
import compose.multiplatform.ui.Modifier
import model.name
import platform.CurrentPalette
import platform.LyricalTheme

@Composable
fun QuestionAppBar(
    questionIndex: Int,
    amountOfSongs: Int,
    sourcePlaylist: SourcePlaylist,
    currentPoints: Double,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    AppBar(
        title = "Question ${questionIndex + 1}/$amountOfSongs",
        subtitle = when (sourcePlaylist) {
            is SourcePlaylist.Shown -> sourcePlaylist.playlist.name
            else -> null
        },
        modifier = modifier,
        onNavigateBack = onNavigateBack
    ) {
        Text(
            text = "$currentPoints/$questionIndex pts",
            style = LyricalTheme.typography.subtitle1,
            color = CurrentPalette.contentSecondary
        ) //TODO: create AnnotatedString with different colors for "/n pts"
    }
}