package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.Color
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.core.graphics.implementation
import compose.multiplatform.ui.unit.implementation
import androidx.compose.foundation.border
import compose.multiplatform.ui.shape.Shape
import compose.multiplatform.ui.shape.implementation

actual fun Modifier.border(width: Dp, color: Color, shape: Shape): Modifier = castOrCreate().apply {
    modifier = modifier.border(width.implementation, color.implementation, shape.implementation)
}
