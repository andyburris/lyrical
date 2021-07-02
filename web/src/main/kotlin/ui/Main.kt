package ui

import AuthAction
import BrowserState
import GameScreen
import com.github.mpetuska.khakra.react.ChakraProvider
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import react.*
import react.dom.div
import react.dom.render
import react.router.dom.hashRouter
import react.router.dom.route
import react.router.dom.switch
import ui.demo.DemoScreen
import toDemoGenre
import ui.game.GameScreen
import ui.khakra.extendTheme
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
                if (gameState !is GameScreen.Unstarted) props.history.replace("/game")
                when {
                    window.localStorage.getItem("access_token") == null -> LandingScreen() { browserState.handleAction(it) }
                    else -> SetupScreen(setupScreen) { browserState.handleAction(it) }
                }
            }

            route<RProps>("/game") { props ->
                when(gameState) {
                    GameScreen.Unstarted -> { props.history.replace("/"); emptyContent() }
                    is GameScreen.Loading -> when (loadingScreen) {
                        null -> emptyContent()
                        else -> LoadingScreen(loadingScreen)
                    }
                    is GameScreen.Playing -> when(gameScreen) {
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