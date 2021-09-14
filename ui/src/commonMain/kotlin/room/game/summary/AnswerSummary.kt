package room.game.summary

import GameAnswer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import client.ClientGameQuestion
import common.Divider
import common.Icon
import model.name
import org.jetbrains.compose.common.foundation.border
import org.jetbrains.compose.common.foundation.clickable
import org.jetbrains.compose.common.foundation.layout.*
import org.jetbrains.compose.common.ui.*
import org.jetbrains.compose.common.ui.unit.dp
import platform.*
import points

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
fun AnswerSummary(
    questions: List<ClientGameQuestion.Answered>,
    modifier: Modifier = Modifier
) {
    Column(modifier) { //TODO: add Arrangement.SpacedBy(32.dp)
        Text("Answers".uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onPrimarySecondary)
        questions.forEachIndexed { index, question ->
            AnswerSummaryItem(question, index)
        }
    }
}

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
private fun AnswerSummaryItem(question: ClientGameQuestion.Answered, index: Int, modifier: Modifier = Modifier) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val padding = if (expanded) 24.dp else 0.dp //TODO: add animateAsState
    Column(
        modifier = modifier
            .clickable { setExpanded(!expanded) }
            .border(1.dp, if (expanded) LyricalTheme.colors.onPrimaryTernary else LyricalTheme.colors.primary)
            .padding(padding) //TODO: add animation, change else to Color.Transparent, LyricalTheme.shapes.medium
    ) {
        AnswerSummaryItemHeader(question, index, expanded, Modifier.fillMaxWidth())
        if (expanded) { //TODO: switch with AnimatedVisibility(expanded)
            AnswerSummaryInfo(question)
        }
    }
}

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
private fun AnswerSummaryItemHeader(question: ClientGameQuestion.Answered, index: Int, expanded: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start, //TODO: add Arrangement.SpacedBy(8.dp)
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start, //TODO: add Arrangement.SpacedBy(16.dp)
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(32.dp)) {
                Image(
                    resource = Resource.Url(question.track.track.album.imageURL),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(1f) //TODO: add Modifier.fillMaxSize()
                )
                Box(Modifier.fillMaxWidth().fillMaxHeight(1f).background(LyricalTheme.colors.overlayDark)) {}
                Text((index + 1).toString(), style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentPrimary) //TODO: add Modifier.align(Alignment.Center)
            }
            Text(question.track.track.name, style = LyricalTheme.typography.h2, color = CurrentPalette.contentPrimary)
        }
        when(question.answer) {
            is GameAnswer.Answered.Correct -> Text("+${question.answer.points} pts", style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentPrimary)
            is GameAnswer.Answered.Incorrect -> Text("Incorrect", style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary)
            is GameAnswer.Answered.Skipped -> Text("Skipped", style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary)
            is GameAnswer.Answered.Missed -> Text("Missed", style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary)
        }

        val rotation = if (expanded) 90f else 0f //TODO: add animation
        Icon(
            icon = Icon.Arrow.Right,
            contentDescription = if (expanded) "Collapse" else "Expand",
            modifier = Modifier.graphicsLayer(rotationZ = rotation),
            tint = CurrentPalette.contentSecondary,
        )
    }
}

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
private fun AnswerSummaryInfo(question: ClientGameQuestion.Answered, modifier: Modifier = Modifier) {
    Column(modifier) { //TODO: add Arrangement.SpacedBy(24.dp)
        Column() { //TODO: add Arrangement.SpacedBy(16.dp)
            Row( //TODO: add Arrangement.SpacedBy(16.dp)
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon = Icon.Quote, contentDescription = null)
                Text("\"${question.allLyrics}\"", style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentPrimary)
            }
            Row( //TODO: add Arrangement.SpacedBy(16.dp)
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon = Icon.Person, contentDescription = null)
                Text(question.track.track.artists.joinToString { it.name }, style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentPrimary)
            }
            Row(//TODO: add Arrangement.SpacedBy(16.dp)
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon = Icon.Playlist, contentDescription = null)
                Text("from ${question.track.sourcePlaylist.name}", style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentPrimary) //TODO: add AnnotatedString for colors
            }
        }
        Divider(CurrentPalette.contentTernary)
        Column() { //TODO: add Arrangement.SpacedBy(16.dp)
            if (question.answer is GameAnswer.Answered.Incorrect) {
                Column {
                    Text("You Said", style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary)
                    Text((question.answer as GameAnswer.Answered.Incorrect).answer, style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentPrimary)
                }
            }
            //TODO: add timer
            Column {
                Text("Hints Used", style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentSecondary)
                val hints = question.answer.hintsUsed.map { hint ->
                    when(hint) {
                        Hint.NextLyric -> "Next Line"
                        Hint.Artist -> "Show Artist"
                    }
                }
                val text = when {
                    hints.isNotEmpty() -> hints.joinToString()
                    else -> "None"
                }
                Text(text, style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentPrimary)
            }
        }
    }
}

