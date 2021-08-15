package styles

import androidx.compose.ui.text.platform.Typeface
import org.jetbrains.compose.common.ui.unit.TextUnit
import org.jetbrains.compose.common.ui.unit.implementation
import org.jetbrains.skija.Typeface
import styles.text.*
import java.io.File

val TextStyle.implementation
    get() = androidx.compose.ui.text.TextStyle(
        fontSize = fontSize.implementation,
        fontWeight = fontWeight?.implementation,
        fontStyle = fontStyle?.implementation,
        fontFamily = fontFamily?.implementation,
        letterSpacing = letterSpacing.implementation,
        lineHeight = lineHeight.implementation
    )

val FontWeight.implementation get() = androidx.compose.ui.text.font.FontWeight(weight = weight)
val FontStyle.implementation get() = androidx.compose.ui.text.font.FontStyle(value = value)
val FontFamily.implementation
    get() = androidx.compose.ui.text.font.FontFamily(
        Typeface(
            typeface = try {
                Typeface.makeFromFile(filePath)
                //throw Exception()
            } catch (e: Exception) {
                println("Can't load typeface $filePath, (current path = ${java.io.File(".").canonicalPath})")
                Typeface.makeDefault()
            },
            alias = alias
        )
    )

val TextOverflow.implementation get() = when(this) {
    TextOverflow.Clip -> androidx.compose.ui.text.style.TextOverflow.Clip
    TextOverflow.Ellipsis -> androidx.compose.ui.text.style.TextOverflow.Ellipsis
    TextOverflow.Visible -> androidx.compose.ui.text.style.TextOverflow.Visible
}