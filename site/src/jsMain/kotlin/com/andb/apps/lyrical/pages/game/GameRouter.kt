package com.andb.apps.lyrical.pages.game

import GameAction
import GameOptions
import Screen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.components.sections.GameAppBar
import com.andb.apps.lyrical.pages.game.answer.AnswerPage
import com.andb.apps.lyrical.pages.game.end.EndPage
import com.andb.apps.lyrical.pages.game.loading.LoadingPage
import com.andb.apps.lyrical.pages.game.question.QuestionPage
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.rememberPageContext
import isRight

@Composable
fun GameRouter(
    screen: Screen.GameScreen,
    modifier: Modifier = Modifier,
    onGameAction: (GameAction) -> Unit,
) {


    when(screen) {
        is Screen.GameScreen.Loading -> LoadingPage(screen, modifier) { onGameAction(it) }
        is Screen.GameScreen.Question -> QuestionPage(
            questionScreen = screen,
            modifier = modifier,
            onAnswer = { onGameAction(it) },
            onRequestHint = { onGameAction(it) },
        )
        is Screen.GameScreen.Answer -> AnswerPage(screen, modifier) { onGameAction(it) }
        is Screen.GameScreen.End -> EndPage(screen, modifier)
    }


}