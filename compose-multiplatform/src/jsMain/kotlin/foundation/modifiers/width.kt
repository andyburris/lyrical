package compose.multiplatform.foundation.layout

import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width

actual fun Modifier.width(size: Dp): Modifier = this then StyleModifier {
    width(size.value.px)
}
