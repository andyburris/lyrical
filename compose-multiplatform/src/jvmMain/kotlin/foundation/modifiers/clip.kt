package org.jetbrains.compose.common.ui.draw

import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.Shape
import compose.multiplatform.ui.shape.implementation
import org.jetbrains.compose.common.internal.castOrCreate
import androidx.compose.ui.draw.clip

actual fun Modifier.clip(shape: Shape): Modifier = castOrCreate().apply {
    modifier = modifier.clip(shape.implementation)
}
