package common

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.Text
import data.LocalTerminal

@Composable
fun Divider() {
    val terminal = LocalTerminal.current
    val width = terminal.width
    val dividerText = (0 until width).joinToString(separator = "") { "-" }
    Text(dividerText)
}