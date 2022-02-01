package compose.multiplatform.foundation.layout

import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.css.percent

actual fun Modifier.fillMaxWidth(fraction: Float): Modifier = this then StyleModifier {
    width((fraction * 100).percent)
}
