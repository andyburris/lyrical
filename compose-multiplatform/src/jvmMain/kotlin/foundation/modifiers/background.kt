package compose.multiplatform.foundation.modifier

import androidx.compose.foundation.background
import compose.multiplatform.ui.Color
import org.jetbrains.compose.common.core.graphics.implementation
import org.jetbrains.compose.common.internal.castOrCreate
import compose.multiplatform.ui.Modifier

actual fun Modifier.background(color: Color): Modifier = castOrCreate().apply {
    modifier = modifier.background(color.implementation)
}