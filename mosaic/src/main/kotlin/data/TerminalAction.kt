package data

data class TerminalAction(
    val name: String,
    val keycodes: List<Keycode>,
    val isShown: Boolean = true,
    val onAction: (Keycode) -> Unit
)

fun TerminalAction(
    name: String,
    vararg keycodes: Keycode,
    isShown: Boolean = true,
    onAction: (Keycode) -> Unit
) = TerminalAction(name, keycodes.toList(), isShown, onAction)