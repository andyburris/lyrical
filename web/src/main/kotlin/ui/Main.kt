package ui

import BrowserState
import collectAsState
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.css.*
import kotlinx.css.properties.ms
import kotlinx.css.properties.s
import kotlinx.css.properties.transition
import kotlinx.html.InputType
import react.*
import react.dom.*
import react.router.dom.*
import styled.*
import ui.game.AnswerScreen
import ui.game.EndScreen
import ui.game.QuestionScreen
import ui.landing.LandingScreen
import ui.loading.LoadingScreen
import ui.setup.SetupScreen

val styles = CSSBuilder().apply {
    fontFace {
        fontFamily = "YoungSerif"
        put("src", "url('assets/fonts/YoungSerif.otf')")
    }
    body {
        margin(0.px)
        padding(0.px)
        backgroundColor = theme.background
    }
    button {
        backgroundColor = theme.primary
        color = theme.onPrimary
        border = "none"
        borderRadius = 32.px
        padding(horizontal = 24.px, vertical = 16.px)
        fontFamily = "Inter"
        fontWeight = FontWeight.w700
        fontSize = 24.px
        cursor = Cursor.pointer
        transition("box-shadow", duration = 200.ms)
        hover {
            shadow(1)
        }
    }
    h1 {
        fontFamily = "YoungSerif"
        fontWeight = FontWeight.w500
        color = theme.onBackground
        fontSize = 64.px
        margin(0.px)
        padding(0.px)
    }
    h2 {
        fontFamily = "YoungSerif"
        fontWeight = FontWeight.w500
        color = theme.onBackground
        fontSize = 48.px
        margin(0.px)
        padding(0.px)
    }
    p {
        fontFamily = "Inter"
        fontSize = 24.px
        fontWeight = FontWeight.w700
        color = theme.onBackgroundSecondary
        margin(0.px)
        padding(0.px)
    }
    input {
        outline = Outline.none
    }
}

fun main() {
    println("is this even updating?")
    injectGlobal(styles.toString())
    render(document.getElementById("root")) {
        child(stack) {}
    }
}

val stack = functionalComponent<RProps> {
    val (browserState, _) = useState(BrowserState(GlobalScope))
    val setupScreen = browserState.currentSetupScreen.collectAsState()
    val gameState = browserState.currentGame.collectAsState()
    val loadingScreen = browserState.currentLoadingScreen.collectAsState()
    val questionScreen = browserState.currentQuestionScreen.collectAsState()
    val answerScreen = browserState.currentAnswerScreen.collectAsState()
    val endScreen = browserState.currentEndScreen.collectAsState()

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
                when(setupScreen) {
                    null -> LandingScreen() { browserState.handleAction(it) }
                    else -> SetupScreen(setupScreen) { browserState.handleAction(it) }
                }
            }
            route<RProps>("/game") { props ->
                when (gameState) {
                    GameState.Unstarted -> props.history.replace("/")
                    is GameState.Loading -> if (props.location.pathname != "/game/loading") props.history.replace("/game/loading")
                    is GameState.Playing -> {
                        when {
                            gameState.screen == GameScreen.Question && props.location.pathname != "/game/question" -> props.history.replace("/game/question")
                            gameState.screen == GameScreen.Answer && props.location.pathname != "/game/answer" -> props.history.replace("/game/answer")
                            gameState.screen == GameScreen.End && props.location.pathname != "/game/end" -> props.history.replace("/game/end")
                        }
                    }
                }
                switch {
                    route("/game/loading") {
                        when (loadingScreen) {
                            null -> emptyContent()
                            else -> LoadingScreen(loadingScreen)
                        }

                    }
                    route("/game/question") {
                        when (questionScreen) {
                            null -> emptyContent()
                            else -> QuestionScreen(questionScreen) { browserState.handleAction(it) }
                        }
                    }
                    route("/game/answer") {
                        when (answerScreen) {
                            null -> emptyContent()
                            else -> AnswerScreen(answerScreen) { browserState.handleAction(GameAction.NextQuestion) }
                        }
                    }
                    route("/game/end") {
                        when (endScreen) {
                            null -> emptyContent()
                            else -> EndScreen(endScreen) { browserState.handleAction(GameAction.RestartGame) }
                        }
                    }
                }
            }
        }
    }
}

private fun RBuilder.emptyContent() = div { }