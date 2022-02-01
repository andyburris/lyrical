package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import androidx.compose.foundation.layout.fillMaxHeight

actual fun Modifier.fillMaxHeight(fraction: Float): Modifier = castOrCreate().apply {
    modifier = modifier.fillMaxHeight(fraction)
}
