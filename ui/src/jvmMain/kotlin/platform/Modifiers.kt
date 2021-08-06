package platform

import androidx.compose.foundation.layout.RowScope
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.ui.Modifier

actual fun Modifier.weight(weight: Float): Modifier = castOrCreate().apply {
    //modifier = modifier.weight(weight)
}