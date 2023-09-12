package com.andb.apps.lyrical.pages.game.question

import GameAction
import GameAnswer
import UserAnswer
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.components.sections.GameAppBar
import com.andb.apps.lyrical.components.widgets.Heading1
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.unaryMinus
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.css.width

@Composable
fun QuestionPage(
    questionScreen: Screen.GameScreen.Question,
    modifier: Modifier = Modifier,
    onAnswer: (GameAction.AnswerQuestion) -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        Column(Modifier.fillMaxHeight()) {
            Column {
                Subtitle("LYRIC", color = LyricalTheme.palette.contentSecondary)
                Heading1("“${questionScreen.lyric}”")
            }
        }
        AnswerSection(
            onAnswer = { onAnswer(GameAction.AnswerQuestion(UserAnswer.Answer(it, false, false))) },
            onSkip = { onAnswer(GameAction.AnswerQuestion(UserAnswer.Skipped(false, false))) },
            modifier = AnswerSectionStyle.toModifier()
        )
    }
}

