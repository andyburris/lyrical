package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.Color
import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.*
import compose.multiplatform.ui.unit.Dp
import org.jetbrains.compose.web.css.*

actual fun Modifier.border(width: Dp, color: Color, shape: Shape): Modifier = this then StyleModifier {
    border(width.value.px, LineStyle.Solid, rgba(color.red, color.green, color.blue, color.alpha))
    margin(-width.value.px) //In CSS, the border has size, but in other Jetpack Compose implementations, it does not, so ignore the added margin from the border
    when(shape) {
        is RoundedCornerShape -> borderRadius(
            topLeft = shape.topStart.implementation,
            topRight = shape.topEnd.implementation,
            bottomLeft = shape.bottomStart.implementation,
            bottomRight = shape.bottomEnd.implementation,
            //TODO: rtl support
        )
        else -> throw Error("Shape other than RoundedCornerShape not supported")
    }
}

private val CornerSize.implementation: CSSNumeric get() = when(this) {
    is DpCornerSize -> this.size.value.px
    is PercentCornerSize -> this.percent.percent
    else -> throw Error("CornerSize other than DpCornerSize or PercentCornerSize not supported")
}