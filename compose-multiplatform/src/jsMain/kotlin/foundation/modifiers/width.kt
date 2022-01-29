package org.jetbrains.compose.common.foundation.layout

import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.common.ui.unit.Dp
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width

actual fun Modifier.width(size: Dp): Modifier = this then StyleModifier {
    width(size.value.px)
}
