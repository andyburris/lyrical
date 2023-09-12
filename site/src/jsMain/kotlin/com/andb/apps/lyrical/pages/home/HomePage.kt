package com.andb.apps.lyrical.pages.home

import AuthAction
import BrowserHomeMachine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.pages.home.loggedIn.HomeLoggedIn
import com.andb.apps.lyrical.pages.home.loggedOut.HomeLoggedOut
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.util.handleAuth
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.core.rememberPageContext
import kotlin.random.Random

@Composable
fun HomePage() {
    val coroutineScope = rememberCoroutineScope()
    val router = rememberPageContext().router
    val setupMachine = remember { BrowserHomeMachine(coroutineScope) }
    val homeState = setupMachine.homeScreen.collectAsState()
    handleAuth { setupMachine.handleAuthAction(it) }
    PageLayout("Lyrical - Home") {
        Column(Modifier.gap(LyricalTheme.Spacing.XXL).fillMaxWidth()) {
            HomeHeader()
            when(val screen = homeState.value) {
                is Screen.Home.LoggedIn -> HomeLoggedIn(
                    screen = screen,
                    onUpdateSetupState = { setupMachine.onChangeSetupState(it) },
                    onStartGame = {
                        val gameID = setupMachine.handleStart(it)
                        router.navigateTo("/game/$gameID")
                    }
                )
                Screen.Home.LoggedOut -> HomeLoggedOut() { setupMachine.handleAuthAction(AuthAction.Authenticate(Random.nextInt().toString())) }
            }
        }
    }
}
