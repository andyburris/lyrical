package com.andb.apps.lyrical.pages.game.answer

import GameAction
import Screen
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.Button
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.theme.LyricalPalette
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.onInitialized
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.w3c.dom.HTMLElement

@Composable
fun AnswerPage(
    answerScreen: Screen.GameScreen.Answer,
    palette: LyricalPalette,
    modifier: Modifier = Modifier,
    onNext: (GameAction.NextQuestion) -> Unit,
) {
    Column(
        modifier = modifier
            .gap(LyricalTheme.Spacing.XL)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .alignItems(AlignItems.Center)
                .justifyContent(JustifyContent.SpaceBetween),
        ) {
            when(val answer = answerScreen.answer) {
                is GameAnswer.Answered.Correct -> Column {
                    Heading2("Correct!", color = palette.contentPrimary)
                    Subtitle("+${answer.points}pts", color = palette.contentSecondary)
                }
                is GameAnswer.Answered.Incorrect -> Heading2("Incorrect", color = palette.contentPrimary)
                is GameAnswer.Answered.Skipped -> Heading2("Skipped", color = palette.contentPrimary)
                GameAnswer.Unanswered -> throw Error("Should never show answer screen for an unanswered question")
            }
            Button(
                text = if (answerScreen.questionNumber == answerScreen.game.questions.size - 1) "Summary" else "Next Question",
                palette = if (palette == LyricalTheme.Colors.accentPalette) LyricalTheme.palette else LyricalTheme.Colors.accentPalette,
                modifier = Modifier
                    .onClick { onNext(GameAction.NextQuestion) }
                    .onInitialized { ref -> ref.focus() },
            )

            Box(ref = ref {
                it.focus()
            })
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .backgroundColor(palette.backgroundDark)
                .borderRadius(LyricalTheme.Radii.XL)
                .padding(LyricalTheme.Spacing.XL)
                .gap(LyricalTheme.Spacing.XL)
        ) {
            when(val answer = answerScreen.answer) {
                is GameAnswer.Answered.Correct -> AnswerItem(answerScreen.track.track, palette)
                is GameAnswer.Answered.Incorrect -> {
                    Column(Modifier.gap(LyricalTheme.Spacing.MD)) {
                        Subtitle("Your Answer", color = palette.contentSecondary)
                        Heading2(answer.answer, color = palette.contentPrimary)
                    }
                    Column(Modifier.gap(LyricalTheme.Spacing.MD)) {
                        Subtitle("Correct Answer", color = palette.contentSecondary)
                        AnswerItem(answerScreen.track.track, palette)
                    }
                }
                is GameAnswer.Answered.Skipped -> Column(Modifier.gap(LyricalTheme.Spacing.MD)) {
                    Subtitle("Correct Answer", color = palette.contentSecondary)
                    AnswerItem(answerScreen.track.track, palette)
                }
                GameAnswer.Unanswered -> throw Error("Should never show answer screen for an unanswered question")
            }
        }
    }
}