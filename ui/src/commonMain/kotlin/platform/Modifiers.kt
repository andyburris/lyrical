package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
expect fun Modifier.verticalScroll(): Modifier

@OptIn(ExperimentalComposeWebWidgetsApi::class)
expect fun Modifier.graphicsLayer(
    rotationZ: Float
): Modifier