package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.Color
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.core.graphics.implementation
import compose.multiplatform.ui.unit.implementation
import androidx.compose.foundation.border

actual fun Modifier.border(size: Dp, color: Color): Modifier = castOrCreate().apply {
    modifier = modifier.border(size.implementation, color.implementation)
}
