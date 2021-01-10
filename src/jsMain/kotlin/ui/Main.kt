package ui

import Machine
import collectAsState
import com.adamratzman.spotify.utils.Language
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.css.*
import react.*
import react.dom.*
import styled.*
import ui.setup.SetupScreen

data class Theme(
    val background: Color = Color("#333333"),
    val onBackground: Color = Color("#FFFFFF"),
    val onBackgroundSecondary: Color = Color("rgba(255, 255, 255, .5)"),
    val onBackgroundPlaceholder: Color = Color("rgba(255, 255, 255, .12)"),
    val primary: Color = Color("#1DB954"),
    val onPrimary: Color = Color("#FFFFFF"),
    val overlay: Color = Color("rgba(0, 0, 0, .12)"),
)
val theme = Theme()

val styles = CSSBuilder().apply {
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
    }
    h1 {
        fontFamily = "YoungSerif"
        color = theme.onBackground
        fontSize = 64.px
        margin(0.px)
        padding(0.px)
    }
    h2 {
        fontFamily = "YoungSerif"
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
}

private val playlistURI = "spotify:playlist:07DnxA8uWkBU10h3fubaqd"
private val tristanPlaylist = "spotify:playlist:5TjcPSTcEyuCkpyZrZpVZQ"
fun main() {
    println("is this even updating?")
    injectGlobal(styles.toString())
    render(document.getElementById("root")) {
        child(stack) {}
    }
}

val stack = functionalComponent<RProps> {
    val (machine, _) = useState(Machine(GlobalScope))
    val (currentState, setState) = machine.screen.collectAsState()
    when (currentState) {
        is State.Setup -> SetupScreen(
            currentState,
            onUpdateSetup = {
                //println("updated setup screen = $it")
                machine += Action.UpdateScreen(updatedScreen = it)
            },
            onPlayGame = { playlistURIs, config ->
                machine += Action.StartGame(playlistURIs, config)
            }
        )
        State.Loading -> LoadingScreen()
        is State.GameState.Question -> QuestionScreen(currentState) { answerScreen ->
            machine += Action.OpenScreen(answerScreen)
        }
        is State.GameState.Answer -> AnswerScreen(currentState) {
            val screen = currentState.toScreen()
            machine += Action.OpenScreen(if (screen.isLastAnswer) screen.end() else screen.nextQuestion())
        }
        is State.GameState.End -> EndScreen(currentState) {
            machine += Action.OpenScreen(Screen.Setup(currentState.data.sourcePlaylists, currentState.data.config))
        }
    }
}