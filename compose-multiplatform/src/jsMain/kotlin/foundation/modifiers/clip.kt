package org.jetbrains.compose.common.ui.draw

import compose.multiplatform.ui.shape.*
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.web.css.*

actual fun Modifier.clip(shape: Shape): Modifier = this then StyleModifier {
    when (shape) {
        is RoundedCornerShape -> when(shape.topStart) {
            //TODO: add rtl support
            is PercentCornerSize -> borderRadius(
                topLeft = (shape.topStart as PercentCornerSize).percent.percent,
                topRight = (shape.topEnd as PercentCornerSize).percent.percent,
                bottomLeft = (shape.bottomStart as PercentCornerSize).percent.percent,
                bottomRight = (shape.bottomEnd as PercentCornerSize).percent.percent,
            )
            is DpCornerSize -> borderRadius(
                topLeft = (shape.topStart as DpCornerSize).size.value.px,
                topRight = (shape.topEnd as DpCornerSize).size.value.px,
                bottomLeft = (shape.bottomStart as DpCornerSize).size.value.px,
                bottomRight = (shape.bottomEnd as DpCornerSize).size.value.px,
            )
        }
    }
    overflow("clip")
}
