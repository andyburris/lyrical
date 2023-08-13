package com.andb.apps.lyrical.pages.home

import BrowserHomeMachine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.andb.apps.lyrical.components.layouts.PageLayout

@Composable
fun HomePage() {
    val coroutineScope = rememberCoroutineScope()
    val setupMachine = remember { BrowserHomeMachine(coroutineScope) { playlists, config -> } }
    val homeState = setupMachine.homeState.collectAsState()
    PageLayout("Lyrical - Home") {
        HomeHeader()
        when(homeState.value) {
            is State.Home.LoggedIn -> TODO()
            State.Home.LoggedOut -> TODO()
        }
    }
}
