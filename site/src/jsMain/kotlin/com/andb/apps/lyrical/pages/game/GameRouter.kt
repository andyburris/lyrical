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
    val router = rememberPageContext().router
    val currentPalette = LyricalTheme.palette
    val palette = remember(screen, currentPalette) {
        when {
            screen is Screen.GameScreen.End || (screen is Screen.GameScreen.Answer && screen.answer.isRight) -> LyricalTheme.Colors.accentPalette
            else -> currentPalette
        }
    }

    PageLayout("Lyrical - Game", modifier.backgroundColor(palette.background)) {
        Column(
            modifier = Modifier
                .gap(LyricalTheme.Spacing.XXL)
                .fillMaxSize()
        ) {
            GameAppBar(
                gameScreen = screen,
                palette = palette,
                modifier = Modifier.fillMaxWidth(),
                onClose = { router.navigateTo("/") }
            )
            when(screen) {
                is Screen.GameScreen.Loading -> LoadingPage(screen, modifier)
                is Screen.GameScreen.Question -> QuestionPage(screen, modifier) { onGameAction(it) }
                is Screen.GameScreen.Answer -> AnswerPage(screen, palette, modifier) { onGameAction(it) }
                is Screen.GameScreen.End -> EndPage(screen, palette, modifier)
            }
        }
    }
}