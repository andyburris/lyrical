package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import androidx.compose.foundation.layout.fillMaxWidth

actual fun Modifier.fillMaxWidth(): Modifier = castOrCreate().apply {
    modifier = modifier.fillMaxWidth()
}
