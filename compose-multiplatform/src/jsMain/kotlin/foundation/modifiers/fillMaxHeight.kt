package org.jetbrains.compose.common.foundation.layout

import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent

actual fun Modifier.fillMaxHeight(fraction: Float): Modifier = this then StyleModifier {
    height((100 * fraction).percent)
}
