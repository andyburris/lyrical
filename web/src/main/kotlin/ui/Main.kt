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
            val subtitle1 = styleBlock {
                "fontFamily" to "Inter"
                "fontSize".toBreakpoints("1rem", "1.25rem", "1.5rem")
                "fontWeight" to "bold"
                "lineHeight" to "115%"
            }
            val heading1 = styleBlock {
                "fontFamily" to "YoungSerif"
                "fontWeight" to "500"
                "fontSize".toBreakpoints("2.5rem", "3rem", "4rem")
                "lineHeight" to "115%"
            }
            val heading2 = styleBlock {
                "fontFamily" to "YoungSerif"
                "fontWeight" to "500"
                "fontSize".toBreakpoints("1.5rem", "2rem", "2.5rem")
                "lineHeight" to "115%"
            }
            breakpoints {
                "md" to "60em"
                "lg" to "70em"
            }
            colors {
                "brandLight" toObject {
                    "background" to "#FFFFFF"
                    "backgroundDark" to "#F0F0F0"
                    "backgroundCard" to "#FFFFFF"
                    "onBackground" to "rgba(0, 0, 0, .87)"
                    "onBackgroundSecondary" to "rgba(0, 0, 0, .5)"
                    "onBackgroundTernary" to "rgba(0, 0, 0, .20)"
                    "primary" to "#1DB954"
                    "primaryDark" to "#1BAA4D"
                    "primaryLight" to "#38C169"
                    "onPrimary" to "#FFFFFF"
                    "onPrimarySecondary" to "rgba(255, 255, 255, .5)"
                    "onPrimaryTernary" to "rgba(255, 255, 255, .20)"
                    "overlay" to "rgba(0, 0, 0, .05)"
                    "darkOverlay" to "rgba(0, 0, 0, .70)"
                }
                "brandDark" toObject {
                    "background" to "#333333"
                    "backgroundDark" to "#2D2D2D"
                    "backgroundCard" to "#545454"
                    "onBackground" to "#FFFFFF"
                    "onBackgroundSecondary" to "rgba(255, 255, 255, .5)"
                    "onBackgroundTernary" to "rgba(255, 255, 255, .20)"
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
                "Button" toSinglePartComponent {
                    variants {
                        "solid" toReactive { props ->
                            "bg" to props.colorMode.value("brandLight.primary", "brandDark.primary")
                            "color" to props.colorMode.value("brandLight.onPrimary", "brandDark.onPrimary")
                            "_hover" toObject {
                                "bg" to props.colorMode.value("brandLight.primaryDark", "brandDark.primaryDark")
                            }
                            "_active" toObject {
                                "bg" to props.colorMode.value("brandLight.primaryDark", "brandDark.primaryDark")
                            }
                        }
                        "solidSecondary" toReactive { props ->
                            "bg" to props.colorMode.value("brandLight.backgroundCard", "brandDark.backgroundCard")
                            "color" to props.colorMode.value("brandLight.onBackgroundSecondary", "brandDark.onBackgroundSecondary")
                            "_hover" toObject {
                                "bg" to props.colorMode.value("brandLight.backgroundDark", "brandDark.backgroundDark")
                            }
                        }
                        "outline" toReactive { props ->
                            "border" to "2px solid"
                            "borderColor" to props.colorMode.value("brandLight.primary", "brandLight.primary")
                            "color" to props.colorMode.value("brandLight.primary", "brandLight.primary")
                            "_hover" toObject {
                                "borderColor" to props.colorMode.value("brandLight.primaryDark", "brandLight.primaryDark")
                                "color" to props.colorMode.value("brandLight.primaryDark", "brandLight.primaryDark")
                            }
                        }
                    }
                    sizes {
                        "fab" toObject {
                            "borderRadius" to "full"
                            "height".toBreakpoints("48", "56", "64")
                            "px" to "24"
                            "position".toBreakpoints("fixed", "static")
                            "bottom".toBreakpoints("32", "0")
                            "right".toBreakpoints("32", "0")
                            subtitle1()
                            "zIndex" to "docked"
                        }
                        "chip" toObject {
                            "borderRadius" to "full"
                            "fontSize".toBreakpoints("1rem", "1.5rem")
                            "px".toBreakpoints("8", "12")
                            "py" to "4"
                        }
                    }
                }
                "Input".toMultiPartComponent(listOf("field", "addon")) {
                    variants {
                        "filled" toObject { props ->
                            "field" toObject {
                                "bg" to props.colorMode.value("brandLight.backgroundCard", "brandDark.backgroundCard")
                                "borderRadius" to "full"
                            }
                        }
                        "unstyled" toObject {
                            "field" toObject {
                                "background" to "transparent"
                            }
                        }
                    }
                    sizes {
                        "sm" toObject {
                            "px" to "2"
                        }
                        "lg" toObject {
                            "field" toObject {
                                "h" to "3rem"
                                subtitle1()
                            }
                            "addon" toObject {
                                "h" to "48"
                                "pl" to "8"
                            }
                        }
                        "xl" toObject {
                            "field" toObject {
                                "h".toBreakpoints("3rem", "4rem")
                                subtitle1()
                            }
                            "addon" toObject {
                                "h" to "4rem"
                                "w" to "4rem"
                                "pl" to "1rem"
                                "pr" to "0rem"
                            }
                        }
                        "unstyled" toObject {
                            "field" toObject {
                                heading1()
                            }
                        }
                    }
                }
            }
            radii {
                "0" to "0"
                "2" to "0.125rem"
                "4" to "0.25rem"
                "8" to "0.5rem"
                "12" to "0.75"
                "16" to "1rem"
                "20" to "1.25rem"
                "24" to "1.5rem"
                "32" to "2rem"
                "40" to "2.5rem"
                "48" to "3rem"
                "56" to "3.5rem"
                "64" to "4rem"
                "72" to "4.5rem"
                "80" to "5rem"
                "88" to "5.5rem"
                "96" to "6rem"
                "112" to "7rem"
                "128" to "8rem"
            }
            space {
                "0" to "0"
                "2" to "0.125rem"
                "4" to "0.25rem"
                "8" to "0.5rem"
                "12" to "0.75rem"
                "16" to "1rem"
                "20" to "1.25rem"
                "24" to "1.5rem"
                "32" to "2rem"
                "40" to "2.5rem"
                "48" to "3rem"
                "56" to "3.5rem"
                "64" to "4rem"
                "72" to "4.5rem"
                "80" to "5rem"
                "88" to "5.5rem"
                "96" to "6rem"
                "112" to "7rem"
                "128" to "8rem"
            }
            layerStyles {
                "card" toObject {
                    "backgroundColor".toReactive("brandLight.backgroundDark", "brandDark.backgroundDark")
/*                    ".chakra-ui-dark &" toObject {
                        "backgroundColor" to "brandDark.backgroundDark"
                        "color" to "brandDark.onBackground"
                    }*/
                    "color".toReactive("brandLight.onBackground", "brandDark.onBackground")
                    "borderRadius" to "16"
                    "p".toBreakpoints("24", "32")
                    "transition" to "background-color 200ms"
                }
                "primaryCard" toObject {
                    "backgroundColor" to "brandDark.primary"
                    "color" to "brandDark.onPrimary"
                    "borderRadius" to "16"
                    "p".toBreakpoints("24", "32")
                }
            }
            textStyles {
                "h1" toObject {
                    heading1()
                }
                "h2" toObject {
                    heading2()
                }
                "sectionHeader" toObject {
                    subtitle1()
                    "fontWeight" to "bold"
                }
                "subtitle1" toObject {
                    subtitle1()
                }
                "subtitle2" toObject {
                    "fontFamily" to "Inter"
                    "fontWeight" to "bold"
                    "lineHeight" to "115%"
                    "fontSize".toBreakpoints("0.875rem", "1rem", "1.25rem")
                }
                "body2" toObject {
                    "fontFamily" to "Inter"
                    "lineHeight" to "115%"
                    "fontSize".toBreakpoints("0.875rem", "1rem", "1.25rem")
                }
            }
            globalStyles { props ->
                "body" toObject {
                    "bgColor" to if (props.colorMode == ColorMode.Dark) "brandDark.background" else "brandLight.background"
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