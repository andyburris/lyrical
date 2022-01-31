package platform

import androidx.compose.ui.graphics.graphicsLayer
import org.jetbrains.compose.common.internal.castOrCreate
import compose.multiplatform.ui.Modifier

actual fun Modifier.graphicsLayer(
    rotationZ: Float
): Modifier = castOrCreate().apply {
    modifier = modifier.graphicsLayer(rotationZ = rotationZ)
}