package data

import Action
import UserAction

data class TerminalAction(
    val name: String,
    val keycodes: List<Keycode>,
    val onAction: (Keycode) -> Unit
)

fun TerminalAction(
    name: String,
    vararg keycodes: Keycode,
    onAction: (Keycode) -> Unit
) = TerminalAction(name, keycodes.toList(), onAction)