package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.Color
import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.Dp
import org.jetbrains.compose.web.css.*

actual fun Modifier.border(size: Dp, color: Color): Modifier = this then StyleModifier {
    border(size.value.px, LineStyle.Solid, rgba(color.red, color.green, color.blue, color.alpha))
    margin(-size.value.px) //In CSS, the border has size, but in other Jetpack Compose implementations, it does not, so ignore the added margin from the border
}
