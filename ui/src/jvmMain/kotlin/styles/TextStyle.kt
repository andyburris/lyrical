package styles

import org.jetbrains.compose.common.ui.unit.TextUnit
import org.jetbrains.compose.common.ui.unit.implementation
import styles.text.FontStyle
import styles.text.FontWeight
import styles.text.TextStyle

val TextStyle.implementation get() = androidx.compose.ui.text.TextStyle(
    fontSize = fontSize.implementation,
    fontWeight = fontWeight?.implementation,
    fontStyle = fontStyle?.implementation,
    letterSpacing = letterSpacing.implementation,
    lineHeight = lineHeight.implementation
)

val FontWeight.implementation get() = androidx.compose.ui.text.font.FontWeight(weight = weight)
val FontStyle.implementation get() = androidx.compose.ui.text.font.FontStyle(value = value)