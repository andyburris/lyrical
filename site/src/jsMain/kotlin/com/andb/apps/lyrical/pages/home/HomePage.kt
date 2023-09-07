package com.andb.apps.lyrical.pages.home

import BrowserHomeMachine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.pages.home.loggedOut.HomeLoggedOut
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap

@Composable
fun HomePage() {
    val coroutineScope = rememberCoroutineScope()
    val setupMachine = remember { BrowserHomeMachine(coroutineScope) { playlists, config -> } }
    val homeState = setupMachine.homeState.collectAsState()
    PageLayout("Lyrical - Home") {
        Column(Modifier.gap(LyricalTheme.Spacing.XXL).fillMaxWidth()) {
            HomeHeader()
            when(homeState.value) {
                is State.Home.LoggedIn -> TODO()
                State.Home.LoggedOut -> HomeLoggedOut()
            }
        }
    }
}
