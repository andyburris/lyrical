package platform

import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.CircleShape
import jetbrains.compose.common.shapes.Shape
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    shape: Shape = CircleShape,
    palette: Palette = CurrentPalette.invert(),
    content: @Composable () -> Unit
) = ActualFloatingActionButton(onClick, modifier, isEnabled, shape, palette, content)

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
expect fun ActualFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean ,
    shape: Shape ,
    palette: Palette,
    content: @Composable () -> Unit
)