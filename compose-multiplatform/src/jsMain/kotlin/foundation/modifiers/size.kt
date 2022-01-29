package org.jetbrains.compose.common.ui

import org.jetbrains.compose.common.internal.ActualModifier
import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.common.ui.unit.Dp
import org.jetbrains.compose.web.css.add
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px

actual fun Modifier.size(width: Dp, height: Dp): Modifier = this then StyleModifier {
    width(width.value.px)
    height(height.value.px)
}
