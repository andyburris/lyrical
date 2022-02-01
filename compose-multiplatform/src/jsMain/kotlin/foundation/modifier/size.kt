package org.jetbrains.compose.common.ui

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.unit.Dp
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px

actual fun Modifier.size(width: Dp, height: Dp): Modifier = this then StyleModifier {
    width(width.value.px)
    height(height.value.px)
}
