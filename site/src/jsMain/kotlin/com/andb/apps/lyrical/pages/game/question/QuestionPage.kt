package com.andb.apps.lyrical.pages.game.question

import GameAction
import GameAnswer
import GameHint
import UserAnswer
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.components.sections.GameAppBar
import com.andb.apps.lyrical.components.widgets.Heading1
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.JustifyContent
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun QuestionPage(
    questionScreen: Screen.GameScreen.Question,
    modifier: Modifier = Modifier,
    onRequestHint: (GameAction.RequestHint) -> Unit,
    onAnswer: (GameAction.AnswerQuestion) -> Unit,
) {
    Column(
        modifier = modifier
            .backgroundColor(LyricalTheme.palette.background)
            .width(100.vw)
            .height(100.vh),
    ){
        val router = rememberPageContext().router
        PageLayout(
            title = "Lyrical - Question ${questionScreen.questionNumber + 1}",
            modifier = Modifier
                .fillMaxHeight()
                .flexGrow(1)
                .overflow {
                    y(Overflow.Scroll)
                }
        ) {
            Column(
                modifier = Modifier
                    .gap(LyricalTheme.Spacing.XXL)
                    .fillMaxSize()
            ) {
                GameAppBar(
                    gameScreen = questionScreen,
                    modifier = Modifier.fillMaxWidth(),
                    onClose = { router.navigateTo("/") },
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .gap(LyricalTheme.Spacing.MD)
                    ) {
                        Column(Modifier.fillMaxWidth()) {
                            Subtitle("LYRIC", color = LyricalTheme.palette.contentSecondary)
                            Heading1 {
                                Span { Text("“${questionScreen.lyric}") }
                                if (GameHint.NextLine in questionScreen.question.answer.hintsUsed) {
                                    Span(Modifier.color(LyricalTheme.palette.contentSecondary).toAttrs()) { Text(" / ") }
                                    Span { Text(questionScreen.nextLyric) }
                                }
                                Span { Text("”") }
                            }
                            if (GameHint.Artist in questionScreen.question.answer.hintsUsed) {
                                Heading2(
                                    text = "- ${questionScreen.artist}",
                                    color = LyricalTheme.palette.contentSecondary,
                                    modifier = Modifier.fillMaxWidth().textAlign(TextAlign.End),
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .gap(LyricalTheme.Spacing.MD)
                                .justifyContent(JustifyContent.End)
                        ) {
                            GameHint.entries.filter { it !in questionScreen.question.answer.hintsUsed }.forEach { hint ->
                                HintChip(hint, modifier = Modifier.onSubmit { onRequestHint.invoke(GameAction.RequestHint(hint)) })
                            }
                        }
                    }
                }
            }
        }
        AnswerSection(
            onAnswer = { onAnswer(GameAction.AnswerQuestion(UserAnswer.Answer(it, questionScreen.question.answer.hintsUsed))) },
            onSkip = { onAnswer(GameAction.AnswerQuestion(UserAnswer.Skipped(questionScreen.question.answer.hintsUsed))) },
            modifier = AnswerSectionStyle.toModifier()
        )
    }
}

@Composable
fun HintChip(
    hint: GameHint,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .cursor(Cursor.Pointer)
            .border(1.px, LineStyle.Solid, LyricalTheme.palette.divider)
            .backgroundColor(LyricalTheme.palette.backgroundCard)
            .padding(leftRight = LyricalTheme.Spacing.MD, topBottom = LyricalTheme.Spacing.SM)
            .borderRadius(LyricalTheme.Radii.Circle),
    ) {
        Subtitle(
            text = when(hint) {
                GameHint.Artist -> "+ Show Artist"
                GameHint.NextLine -> "+ Next Line"
            }
        )
    }
}