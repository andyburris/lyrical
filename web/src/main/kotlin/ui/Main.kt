package ui

import BrowserState
import collectAsState
import com.github.mpetuska.khakra.react.ChakraProvider
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.css.*
import kotlinx.css.properties.ms
import kotlinx.css.properties.transition
import react.*
import react.dom.*
import react.router.dom.*
import ui.game.AnswerScreen
import ui.game.EndScreen
import ui.game.QuestionScreen
import ui.khakra.*
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
        overflowX = Overflow.hidden
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
    //injectGlobal(styles.toString())
    render(document.getElementById("root")) {
        val customTheme2 = createTheme {
            colors {
                "brand" toObject {
                    "background" to "#333333"
                    "backgroundDark" to "#2D2D2D"
                    "backgroundCard" to "#545454"
                    "onBackground" to "#FFFFFF"
                    "onBackgroundSecondary" to "rgba(255, 255, 255, .5)"
                    "onBackgroundTernary" to "rgba(255, 255, 255, .12)"
                    "primary" to "#1DB954"
                    "primaryDark" to "#1BAA4D"
                    "primaryLight" to "#38C169"
                    "onPrimary" to "#FFFFFF"
                    "onPrimarySecondary" to "rgba(255, 255, 255, .5)"
                    "onPrimaryTernary" to "rgba(255, 255, 255, .20)"
                    "overlay" to "rgba(0, 0, 0, .12)"
                    "darkOverlay" to "rgba(0, 0, 0, .70)"
                }
            }
            config {
                initialColorMode = ColorMode.Dark
            }
            components {
                "Button" toComponent {
                    baseStyle {
                        "borderRadius" to "full"
                        "h" to "16"
                    }
                    variants {
                        "solid" toObject {
                            "bg" to "brand.primary"
                            "color" to "brand.onPrimary"
                            "_hover" toObject {
                                "bg" to "brand.primaryDark"
                            }
                        }
                        "outline" toObject {
                            "border" to "2px solid"
                            "borderColor" to "brand.primary"
                            "color" to "brand.primary"
                            "_hover" toObject {
                                "borderColor" to "brand.primaryDark"
                                "color" to "brand.primaryDark"
                            }
                        }
                    }
                }
                "Input" toComponent {
                    baseStyle {
                        "fontSize" to ""
                    }
                    variants {
                        "filled" toObject {
                            "bg" to "brand.backgroundCard"
                            "textStyle" to "subtitle1"
                            "borderRadius" to "full"
                            "px" to "24"
                            "py" to "16"
                        }
                        "unstyled" toObject {
                            "textStyle" to "h1"
                        }
                    }
                }
            }
            layerStyles {
                "card" toObject {
                    "backgroundColor" to "brand.backgroundDark"
                    "color" to "brand.onBackground"
                    "borderRadius" to "lg"
                    "p" to "8"
                }
                "primaryCard" toObject {
                    "backgroundColor" to "brand.primary"
                    "color" to "brand.onPrimary"
                    "borderRadius" to "lg"
                    "p" to "8"
                }
            }
            textStyles {
                "h1" toObject {
                    "fontFamily" to "YoungSerif"
                    "color" to "brand.onBackground"
                    "fontWeight" to "500"
                }
                "h2" toObject {
                    "fontFamily" to "YoungSerif"
                    "color" to "brand.onBackground"
                    "fontWeight" to "500"
                }
                "sectionHeader" toObject {
                    "fontFamily" to "Inter"
                    "color" to "brand.onBackgroundSecondary"
                    "fontWeight" to "bold"
                }
                "subtitle1" toObject {
                    "fontFamily" to "Inter"
                    "color" to "brand.onBackground"
                    "fontWeight" to "bold"
                }
                "subtitle2" toObject {
                    "fontFamily" to "Inter"
                    "color" to "brand.onBackground"
                    "fontWeight" to "bold"
                    "lineHeight" to "100%"
                }
                "body2" toObject {
                    "fontFamily" to "Inter"
                    "color" to "brand.onBackgroundSecondary"
                    "lineHeight" to "100%"
                }
            }
            globalStyles {
                "body" toObject {
                    "bgColor" to "brand.background"
                }
            }
            typography {
                fonts {
                    "heading" to "YoungSerif, -apple-system, BlinkMacSystemFont, \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\""
                    "body" to "Inter, -apple-system, BlinkMacSystemFont, \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\""
                    "mono" to "SFMono-Regular,Menlo,Monaco,Consolas,\"Liberation Mono\",\"Courier New\",monospace"
                }
            }
        }
        val theme = extendTheme(customTheme2)
        println("theme = ${JSON.stringify(theme)}")
        ChakraProvider({this.theme = theme}) {
            child(stack) {}
        }
    }
}

val stack = functionalComponent<RProps> {
    val (browserState, _) = useState { BrowserState(GlobalScope) }
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