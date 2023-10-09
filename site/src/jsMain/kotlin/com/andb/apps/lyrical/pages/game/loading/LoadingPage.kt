package com.andb.apps.lyrical.pages.game.loading

import GameAction
import Screen
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.components.sections.GameAppBar
import com.andb.apps.lyrical.components.widgets.Button
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.CSSAnimation
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.animation.Keyframes
import com.varabyte.kobweb.silk.components.animation.toAnimation
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.AnimationTimingFunction.Companion.cubicBezier

@Composable
fun LoadingPage(
    loadingScreen: Screen.GameScreen.Loading,
    modifier: Modifier = Modifier,
    onReload: (GameAction.Reload) -> Unit,
) {
    val router = rememberPageContext().router
    PageLayout("Lyrical - Loading", modifier) {
        Column(
            modifier = Modifier
                .gap(LyricalTheme.Spacing.XXL)
                .fillMaxSize()
        ) {
            GameAppBar(
                gameScreen = loadingScreen,
                modifier = Modifier.fillMaxWidth(),
                onClose = { router.navigateTo("/") }
            )
            Column(
                modifier = Modifier
                    .gap(LyricalTheme.Spacing.MD)
                    .fillMaxSize()
                    .justifyContent(JustifyContent.Center)
            ) {
                when(loadingScreen.loadingState) {
                    LoadingState.LoadingLyrics -> Heading2("Loading lyrics...")
                    LoadingState.LoadingSongs -> Heading2("Loading songs...")
                    LoadingState.ErrorLoading -> Heading2("Error loading")
                }
                when(loadingScreen.loadingState) {
                    LoadingState.LoadingLyrics, LoadingState.LoadingSongs -> IndeterminateProgressBar()
                    LoadingState.ErrorLoading -> Button("Try Again", modifier = Modifier.onSubmit(isButton = true) { onReload(GameAction.Reload) })
                }
            }
        }
    }
}

