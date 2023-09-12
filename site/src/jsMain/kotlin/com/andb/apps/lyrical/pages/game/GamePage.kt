package com.andb.apps.lyrical.pages.game

import BrowserGameMachine
import GameMachine
import GameStorageState
import LyricsRepository
import SpotifyRepository
import androidx.compose.runtime.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.PageContext
import com.varabyte.kobweb.core.rememberPageContext
import getRepository
import kotlinx.browser.localStorage
import kotlinx.serialization.json.Json
import org.w3c.dom.get

@Page("/game/{game}")
@Composable
fun GamePage() {
    val context = rememberPageContext()
    val gameID = context.route.slug
    val coroutineScope = rememberCoroutineScope()
    val spotifyRepository = remember { getRepository { context.goHome() } }
    val game: GameStorageState? = localStorage[gameID]?.let { Json.decodeFromString<GameStorageState>(it) }
    if (spotifyRepository !is SpotifyRepository.LoggedIn || game == null)  {
        LaunchedEffect(Unit) {
            context.goHome()
        }
    } else {
        val gameMachine = remember {
            BrowserGameMachine(
                coroutineScope = coroutineScope,
                spotifyRepository = spotifyRepository,
                lyricsRepository = LyricsRepository(debug = true),
                gameID = gameID,
                gameStorageState = game
            )
        }
        val screen = gameMachine.gameScreen.collectAsState()
        GameRouter(screen.value) { gameMachine.handleAction(it) }
    }
}

private fun PageContext.goHome() {
    router.navigateTo("/")
}