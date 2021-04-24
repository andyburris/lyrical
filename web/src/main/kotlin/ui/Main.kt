package ui

import BrowserState
import Game
import GameAction
import GameConfig
import GameMachine
import collectAsState
import com.github.mpetuska.khakra.kt.get
import com.github.mpetuska.khakra.react.ChakraProvider
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import react.*
import react.dom.*
import react.router.dom.*
import ui.demo.DemoScreen
import ui.demo.toDemoGenre
import ui.game.AnswerScreen
import ui.game.EndScreen
import ui.game.GameScreen
import ui.game.QuestionScreen
import ui.khakra.*
import ui.landing.LandingScreen
import ui.loading.LoadingScreen
import ui.setup.SetupScreen

fun main() {
    render(document.getElementById("root")) {
        val theme = extendTheme(customTheme())
        ChakraProvider({this.theme = theme}) {
            child(stack) {}
        }
    }
}

val stack = functionalComponent<RProps> {
    val (browserState, _) = useState { BrowserState(GlobalScope) }
    val setupScreen = browserState.setupMachine.currentSetupScreen.collectAsState()
    val gameState = browserState.currentGame.collectAsState()
    val loadingScreen = browserState.currentLoadingScreen.collectAsState()
    val gameScreen = browserState.gameMachine.currentGameScreen.collectAsState()

    hashRouter {
        switch {
            route<RProps>("/:access_token(access_token=.*)") { props ->
                println("auth, hash = ${props.location.pathname}")
                val parameters = props.location.pathname.split("&").map { it.takeLastWhile { it != '=' } }
                println("parameters = $parameters")
                if (parameters.size >= 4) {
                    val (token, tokenType, expiresIn, state) = parameters
                    browserState.handleAction(AuthAction.CheckAuthentication(token, tokenType, expiresIn.toInt(), state))
                }
                props.history.replace("/")
                emptyContent()
            }
            route<RProps>("/", exact = true) { props ->
                if (gameState !is GameState.Unstarted) props.history.replace("/game")
                when {
                    window.localStorage.getItem("access_token") == null -> LandingScreen() { browserState.handleAction(it) }
                    else -> SetupScreen(setupScreen) { browserState.handleAction(it) }
                }
            }

            route<RProps>("/game") { props ->
                when(gameState) {
                    GameState.Unstarted -> { props.history.replace("/"); emptyContent() }
                    is GameState.Loading -> when (loadingScreen) {
                        null -> emptyContent()
                        else -> LoadingScreen(loadingScreen)
                    }
                    is GameState.Playing -> when(gameScreen) {
                        null -> emptyContent()
                        else -> GameScreen(gameScreen) { browserState.handleAction(it) }
                    }
                }
            }

            route<RProps>("/demo") { props ->
                val genre = props.location.search.removePrefix("?genre=").toDemoGenre()
                DemoScreen(genre)
            }
        }
    }
}


fun RBuilder.emptyContent() = div { }