package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.ui.Modifier

@Composable
expect fun Modifier.verticalScroll(): Modifier

expect fun Modifier.graphicsLayer(
    rotationZ: Float
): Modifier