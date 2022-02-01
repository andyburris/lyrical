package compose.multiplatform.foundation.modifiers

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.unit.implementation
import org.jetbrains.compose.common.internal.castOrCreate

actual fun Modifier.height(size: Dp): Modifier = castOrCreate().apply {
    modifier = modifier.requiredHeight(size.implementation) //TODO: decide whether to split multiplatform implementation into height and requiredHeight
}
