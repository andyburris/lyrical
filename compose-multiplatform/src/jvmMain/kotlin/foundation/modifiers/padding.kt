package compose.multiplatform.foundation.modifier

import androidx.compose.foundation.layout.padding
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.unit.implementation

actual fun Modifier.padding(all: Dp): Modifier = castOrCreate().apply {
    modifier = modifier.padding(all.implementation)
}