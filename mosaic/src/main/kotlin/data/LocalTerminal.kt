package data

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import java.awt.event.KeyEvent

val LocalTerminal = staticCompositionLocalOf { ComposeTerminal(TerminalBuilder.terminal()) }

data class ComposeTerminal(val terminal: Terminal) {
    val presses: SharedFlow<Keycode> = terminal
        .also { it.enterRawMode() }
        .reader()
        .buffered()
        .charSequence()
        .asFlow()
        .convertToKeycode()
        .shareIn(CoroutineScope(Dispatchers.IO), started = SharingStarted.Eagerly)

    suspend fun onKeycodePress(keycode: Keycode) = presses.filter { it == keycode }
}

fun Char.toKeycode(): Keycode {
    val isControl = this.isISOControl()
    val letter = when(isControl) {
        true -> this + 64
        false -> this
    }
    val isShift = this.isUpperCase()
    val modifiers = (if (isControl) listOf(Key.CtrlLeft) else emptyList()) + (if (isShift) listOf(Key.ShiftLeft) else emptyList())
    val key = Key(letter.uppercaseChar().code) // the Key class maps the character codes to the correct key only for uppercase letters (plus numbers and symbols)
    return Keycode(modifiers = modifiers, key)
}

fun Flow<Char>.convertToKeycode(): Flow<Keycode> {
    val combination = mutableListOf<Char>()
    return this.transform { char: Char ->
        when(char) {
            13.toChar() -> emit(Key.Enter.pressed())
            27.toChar() -> combination += char
            91.toChar() -> if (combination.lastOrNull() == 27.toChar()) combination += char else emit(char.toKeycode())
            65.toChar() -> {
                if (combination.lastOrNull() == 91.toChar()) emit(Key.DirectionUp.pressed()) else emit(char.toKeycode())
                combination.clear()
            }
            66.toChar() -> {
                if (combination.lastOrNull() == 91.toChar()) emit(Key.DirectionDown.pressed()) else emit(char.toKeycode())
                combination.clear()
            }
            67.toChar() -> {
                if (combination.lastOrNull() == 91.toChar()) emit(Key.DirectionRight.pressed()) else emit(char.toKeycode())
                combination.clear()
            }
            68.toChar() -> {
                if (combination.lastOrNull() == 91.toChar()) emit(Key.DirectionLeft.pressed()) else emit(char.toKeycode())
                combination.clear()
            }
            else -> emit(char.toKeycode())
        }
    }
}
