package compose.multiplatform.foundation.modifiers

import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.Dp
import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px

actual fun Modifier.height(size: Dp): Modifier = this then StyleModifier {
    height(size.value.px)
}