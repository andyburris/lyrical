package org.jetbrains.compose.common.ui

import org.jetbrains.compose.common.ui.unit.Dp
import kotlin.jvm.JvmName

fun Modifier.size(size: Dp): Modifier {
    return size(size, size)
}
expect fun Modifier.size(width: Dp, height: Dp): Modifier
