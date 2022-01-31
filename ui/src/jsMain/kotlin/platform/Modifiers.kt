package platform

import org.jetbrains.compose.common.internal.castOrCreate
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.css.deg


actual fun Modifier.graphicsLayer(
    rotationZ: Float
): Modifier = castOrCreate().apply {
    add {
        property("rotate", rotationZ.deg)
    }
}