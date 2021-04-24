package ui.demo

import Game
import GameConfig
import GameMachine
import GameState
import collectAsState
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import react.*
import ui.game.GameScreen
import ui.loading.LoadingScreen

fun RBuilder.DemoScreen(genre: DemoGenre): ReactElement = child(demoScreen) {
    attrs {
        this.genre = genre
    }
}

external interface DemoProps : RProps {
    var genre: DemoGenre
}
private val demoScreen = functionalComponent<DemoProps> { props ->
    val (gameFlow, _) = useState { MutableStateFlow<GameState>(GameState.Loading(LoadingState.LoadingSongs)) }
    val (gameMachine, _) = useState { GameMachine(GlobalScope, gameFlow) }
    gameMachine.currentGameScreen.onEach {
        println("currentGameScreen changed")
    }
    val gameState = gameMachine.currentGameScreen.collectAsState()

    useEffect(dependencies = emptyList()) {
        println("running demo effect")
        CoroutineScope(Dispatchers.Default).launch {
            val demoGame = Game(
                questions = getDemoQuestions(props.genre),
                config = GameConfig()
            )
            //val demoGame = Game(emptyList(), GameConfig())
            println("loaded demo game")
            gameFlow.value = GameState.Playing(demoGame, GameScreen.Question)
        }
    }

    println("gameState updated = $gameState")
    when(gameState) {
        null -> LoadingScreen(LoadingState.LoadingSongs)
        else -> GameScreen(gameState) {
            when(it) {
                GameAction.RestartGame -> window.history.back()
                else -> gameMachine.handleAction(it)
            }
        }
    }
}

sealed class DemoGenre {
    sealed class Rock : DemoGenre() {
        object AltRock : Rock()
    }
    sealed class Pop : DemoGenre() {
        object Modern : Pop()
    }
    sealed class Rap : DemoGenre() {
        object Modern : Rap()
    }
}

fun String.toDemoGenre() = when(this.toLowerCase()) {
    "altrock" -> DemoGenre.Rock.AltRock
    "pop" -> DemoGenre.Pop.Modern
    "rap" -> DemoGenre.Rap.Modern
    else -> throw Error("\"$this\" is not a properly formatted genre")
}

fun DemoGenre.urlString() = when(this) {
    DemoGenre.Rock.AltRock -> "altrock"
    DemoGenre.Pop.Modern -> "pop"
    DemoGenre.Rap.Modern -> "rap"
}