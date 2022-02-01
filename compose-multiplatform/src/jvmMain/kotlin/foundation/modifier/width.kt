package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import androidx.compose.foundation.layout.width
import compose.multiplatform.ui.unit.implementation

actual fun Modifier.width(size: Dp): Modifier = castOrCreate().apply {
    modifier = modifier.width(size.implementation)
}
