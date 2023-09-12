package com.andb.apps.lyrical.pages.game.loading

import Screen
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.components.sections.GameAppBar
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.core.rememberPageContext

@Composable
fun LoadingPage(
    loadingScreen: Screen.GameScreen.Loading,
    modifier: Modifier = Modifier,
) {
    val router = rememberPageContext().router
    PageLayout("Lyrical - Loading") {
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
            when(loadingScreen.loadingState) {
                is LoadingState.LoadingLyrics -> Subtitle("Loading lyrics...")
                LoadingState.LoadingSongs -> Subtitle("Loading songs...")
            }
        }
    }
}