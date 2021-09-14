package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.flexGrow
import org.jetbrains.compose.web.css.overflowY

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
actual fun Modifier.verticalScroll(): Modifier = castOrCreate().apply {
    add {
        overflowY("scroll")
    }
}

@OptIn(ExperimentalComposeWebWidgetsApi::class)
actual fun Modifier.graphicsLayer(
    rotationZ: Float
): Modifier = castOrCreate().apply {
    add {
        property("rotate", rotationZ.deg)
    }
}