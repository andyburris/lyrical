package room.game.question

import androidx.compose.runtime.Composable
import client.SourcePlaylist
import org.jetbrains.compose.common.ui.Modifier
import platform.LyricalTheme
import platform.Text
import common.AppBar
import model.name
import platform.CurrentPalette
import platform.LocalPalette

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
        Text("$currentPoints/$questionIndex pts", style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary) //TODO: create AnnotatedString with different colors for "/n pts"
    }
}