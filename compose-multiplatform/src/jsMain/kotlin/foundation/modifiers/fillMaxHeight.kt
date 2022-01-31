package compose.multiplatform.foundation.layout

import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent

actual fun Modifier.fillMaxHeight(fraction: Float): Modifier = this then StyleModifier {
    height((100 * fraction).percent)
}
