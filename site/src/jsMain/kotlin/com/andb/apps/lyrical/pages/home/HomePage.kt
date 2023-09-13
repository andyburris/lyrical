package com.andb.apps.lyrical.pages.home

import AuthAction
import BrowserHomeMachine
import androidx.compose.runtime.*
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
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomePage() {
    val coroutineScope = rememberCoroutineScope()
    val router = rememberPageContext().router
    val needsReauthentication = remember { mutableStateOf<AuthAction.Authenticate?>(null) }
    val setupMachine = remember { BrowserHomeMachine(
        coroutineScope = coroutineScope,
        onReauthenticate = { needsReauthentication.value = it }
    ) }
    val homeState = setupMachine.homeScreen.collectAsState()

    handleAuth { setupMachine.handleAuthAction(it) }
    LaunchedEffect(needsReauthentication.value) {
        delay(500.milliseconds)
        println("reauthenticating = ${needsReauthentication.value}")
        when (val authAction = needsReauthentication.value) {
            null -> {}
            else -> setupMachine.handleAuthAction(authAction)
        }
    }

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
