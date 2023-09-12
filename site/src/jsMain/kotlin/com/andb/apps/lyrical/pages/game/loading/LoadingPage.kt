package com.andb.apps.lyrical.pages.game.loading

import Screen
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.varabyte.kobweb.compose.ui.Modifier

@Composable
fun LoadingPage(
    loadingScreen: Screen.GameScreen.Loading,
    modifier: Modifier = Modifier,
) {
    PageLayout("Lyrical - Game", modifier) {
        when(loadingScreen.loadingState) {
            is LoadingState.LoadingLyrics -> Subtitle("Loading lyrics...")
            LoadingState.LoadingSongs -> Subtitle("Loading songs...")
        }
    }
}