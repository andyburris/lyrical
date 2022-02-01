package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import androidx.compose.foundation.clickable

actual fun Modifier.clickable(onClick: () -> Unit): Modifier = castOrCreate().apply {
    modifier = modifier.clickable(onClick = onClick)
}
