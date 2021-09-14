import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi

@OptIn(ExperimentalComposeWebWidgetsApi::class)
fun Int.toColor(): Color = this.toLong().toColor()
@OptIn(ExperimentalComposeWebWidgetsApi::class)
fun Long.toColor(): Color{
    val red = (this and 0x00FF0000) shr 24
    val green = (this and 0x0000FF00) shr 16
    val blue = (this and 0x000000FF) shr 8
    return Color(red.toInt(), green.toInt(), blue.toInt())
}