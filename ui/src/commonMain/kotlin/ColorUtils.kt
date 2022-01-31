import compose.multiplatform.ui.Color

fun Int.toColor(): Color = this.toLong().toColor()
fun Long.toColor(): Color {
    val alpha = (this and 0xFF000000) shr 24
    val red = (this and 0x00FF0000) shr 16
    val green = (this and 0x0000FF00) shr 8
    val blue = (this and 0x000000FF) shr 0
    return Color(red.toInt(), green.toInt(), blue.toInt(), (alpha / 255f))
}