@file:JvmName("PlatformSize")
package org.jetbrains.compose.common.ui

import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.unit.implementation
import androidx.compose.foundation.layout.size
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate

actual fun Modifier.size(width: Dp, height: Dp): Modifier = castOrCreate().apply {
    modifier = modifier.size(width.implementation, height.implementation)
}
