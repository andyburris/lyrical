package ui.game

import GameAction
import react.RBuilder
import react.ReactElement

fun RBuilder.GameScreen(gameState: State.GameState, handleAction: (GameAction) -> Unit): ReactElement {
    return when(gameState) {
        is State.GameState.Question -> QuestionScreen(gameState) { handleAction(it) }
        is State.GameState.Answer -> AnswerScreen(gameState) { handleAction(GameAction.NextQuestion) }
        is State.GameState.End -> EndScreen(gameState) { handleAction(GameAction.RestartGame) }
    }
}
