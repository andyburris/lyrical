package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.implementation
import compose.multiplatform.ui.Color
import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.css.backgroundColor

actual fun Modifier.background(color: Color): Modifier = this then StyleModifier {
    backgroundColor(color.implementation)
}