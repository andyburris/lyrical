package platform

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.graphicsLayer
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.ui.Modifier


@Composable
actual fun Modifier.verticalScroll(): Modifier = castOrCreate().apply {
    modifier = modifier.verticalScroll(rememberScrollState())
}

actual fun Modifier.graphicsLayer(
    rotationZ: Float
): Modifier = castOrCreate().apply {
    modifier = modifier.graphicsLayer(rotationZ = rotationZ)
}