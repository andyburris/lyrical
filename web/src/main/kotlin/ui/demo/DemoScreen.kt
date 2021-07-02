package ui.demo

import DemoGenre
import Game
import GameConfig
import GameMachine
import GameScreen
import getDemoQuestions
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
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
    val (gameFlow, _) = useState { MutableStateFlow<GameScreen>(GameScreen.Loading(LoadingState.LoadingSongs)) }
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
            gameFlow.value = GameScreen.Playing(demoGame, GameScreen.Question)
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

