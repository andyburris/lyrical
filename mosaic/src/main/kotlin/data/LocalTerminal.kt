package data

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import java.awt.event.KeyEvent

val LocalTerminal = staticCompositionLocalOf { ComposeTerminal(TerminalBuilder.terminal()) }

data class ComposeTerminal(val terminal: Terminal) {
    private val reader = terminal.also { it.enterRawMode() }.reader()
    private val shortcuts = mutableListOf<Pair<String, Keycode>>()
    init {
        val currentModifiers = mutableListOf<Key>()
        while (true) {
            val char = reader.read()
            when(char) {

            }
        }
    }
}