package room.game.question

import GameAction
import Hint
import androidx.compose.runtime.Composable
import client.ClientGameQuestion
import client.Hints
import client.artist
import client.nextLyric
import common.Chip
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.foundation.modifier.clickable
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import platform.LyricalTheme

@Composable
fun LyricSection(question: ClientGameQuestion.Unanswered, questionIndex: Int, modifier: Modifier = Modifier, onQuestionAction: (GameAction.Question) -> Unit) {
    Column(modifier = modifier) { //TODO: add Arrangement.SpacedBy(16.dp)
        Column { //TODO: add Arrangement.SpacedBy(4.dp)
            Text("Lyric".uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackgroundSecondary)
            val shownLyrics = question.lyric + (question.hints.nextLyric?.let { " / $it" } ?: "") //TODO: create AnnotatedString
            Text(shownLyrics, style = LyricalTheme.typography.h1, color = LyricalTheme.colors.onBackground)
            question.hints.artist?.let {
                Text(it, style = LyricalTheme.typography.h2, color = LyricalTheme.colors.onBackgroundSecondary)
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (question.hints !is Hints.Both && question.hints !is Hints.Artist){
                Chip(
                    text = "+ Show Artist",
                    modifier = Modifier.clickable {
                        onQuestionAction.invoke(GameAction.Question.RequestHint(questionIndex, Hint.Artist))
                    }
                )
            }
            if (question.hints !is Hints.Both && question.hints !is Hints.NextLyric){
                Chip(
                    text = "+ Next Line",
                    modifier = Modifier.clickable {
                        onQuestionAction.invoke(GameAction.Question.RequestHint(questionIndex, Hint.NextLyric))
                    }
                )
            }
        }
    }
}