package ui.game

import GameAction
import client.Screen
import react.RBuilder
import react.ReactElement

fun RBuilder.GameScreen(gameState: Screen.GameScreen, handleAction: (GameAction) -> Unit): ReactElement {
    return when(gameState) {
        is Screen.GameScreen.Question -> QuestionScreen(gameState) { handleAction(it) }
        is Screen.GameScreen.Answer -> AnswerScreen(gameState) { handleAction(GameAction.NextQuestion) }
        is Screen.GameScreen.End -> EndScreen(gameState) { handleAction(GameAction.RestartGame) }
    }
}
