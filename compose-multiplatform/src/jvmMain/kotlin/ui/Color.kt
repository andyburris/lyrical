package org.jetbrains.compose.common.core.graphics

import compose.multiplatform.ui.Color
import androidx.compose.ui.graphics.Color as JColor

val Color.implementation
    get() = JColor(red, green, blue, (alpha * 255).toInt())