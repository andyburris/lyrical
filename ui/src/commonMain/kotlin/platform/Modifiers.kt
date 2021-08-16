package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.ui.Modifier

expect fun Modifier.weight(weight: Float = 1f): Modifier

@Composable
expect fun Modifier.verticalScroll(): Modifier

expect fun Modifier.graphicsLayer(
    rotationZ: Float
): Modifier