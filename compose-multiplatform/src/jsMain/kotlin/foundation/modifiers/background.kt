package org.jetbrains.compose.common.foundation

import implementation
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.web.css.add
import org.jetbrains.compose.web.css.backgroundColor

actual fun Modifier.background(color: Color): Modifier = this then StyleModifier {
    backgroundColor(color.implementation)
}