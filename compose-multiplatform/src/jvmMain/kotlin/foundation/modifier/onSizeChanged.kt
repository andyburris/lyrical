package org.jetbrains.compose.common.ui.layout

import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.IntSize
import org.jetbrains.compose.common.internal.castOrCreate
import androidx.compose.ui.layout.onSizeChanged

actual fun Modifier.onSizeChanged(
    onSizeChanged: (IntSize) -> Unit
): Modifier = castOrCreate().apply {
    modifier = modifier.onSizeChanged {
        onSizeChanged(IntSize(it.width, it.height))
    }
}
