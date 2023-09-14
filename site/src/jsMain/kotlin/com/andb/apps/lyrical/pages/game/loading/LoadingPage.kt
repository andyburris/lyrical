package com.andb.apps.lyrical.pages.game.loading

import Screen
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.components.sections.GameAppBar
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.theme.LyricalTheme
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
                    is LoadingState.LoadingLyrics -> Heading2("Loading lyrics...")
                    LoadingState.LoadingSongs -> Heading2("Loading songs...")
                }
                IndeterminateProgressBar()
            }
        }
    }
}

