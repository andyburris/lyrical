package org.jetbrains.compose.common.ui

import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.Dp

fun Modifier.size(size: Dp): Modifier {
    return size(size, size)
}
expect fun Modifier.size(width: Dp, height: Dp): Modifier
