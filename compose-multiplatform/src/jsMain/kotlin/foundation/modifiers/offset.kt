package compose.multiplatform.foundation.layout

import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.css.px

actual fun Modifier.offset(x: Dp, y: Dp): Modifier = this then StyleModifier {
    property("transform", "translate(${x.value.px}, ${y.value.px})") //TODO: use transform builder functions when added
}
