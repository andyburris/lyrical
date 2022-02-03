package org.jetbrains.compose.common.ui

import compose.multiplatform.ui.Alignment
import androidx.compose.ui.Alignment as JAlignment

val Alignment.implementation: JAlignment
    get() = when(this) {
        Alignment.TopStart -> JAlignment.TopStart
        Alignment.TopCenter -> JAlignment.TopCenter
        Alignment.TopEnd -> JAlignment.TopEnd
        Alignment.CenterStart -> JAlignment.CenterStart
        Alignment.Center -> JAlignment.Center
        Alignment.CenterEnd -> JAlignment.CenterEnd
        Alignment.BottomStart -> JAlignment.BottomStart
        Alignment.BottomCenter -> JAlignment.BottomCenter
        Alignment.BottomEnd -> JAlignment.BottomEnd
        else -> throw Error("unsupported Alignment")
    }

val Alignment.Vertical.implementation: JAlignment.Vertical
    get() = when (this) {
        Alignment.Top -> JAlignment.Top
        Alignment.CenterVertically -> JAlignment.CenterVertically
        else -> JAlignment.Bottom
    }

val Alignment.Horizontal.implementation: JAlignment.Horizontal
    get() = when(this) {
        Alignment.Start -> JAlignment.Start
        Alignment.CenterHorizontally -> JAlignment.CenterHorizontally
        else -> JAlignment.End
    }