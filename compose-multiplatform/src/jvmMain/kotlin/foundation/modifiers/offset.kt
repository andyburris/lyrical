package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import androidx.compose.foundation.layout.offset
import compose.multiplatform.ui.unit.implementation

actual fun Modifier.offset(x: Dp, y: Dp): Modifier = castOrCreate().apply {
    modifier = modifier.offset(x.implementation, y.implementation)
}
