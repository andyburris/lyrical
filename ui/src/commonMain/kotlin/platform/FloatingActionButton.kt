package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.shape.Shape

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    shape: Shape = CircleShape,
    content: @Composable () -> Unit
) = FloatingActionButton(onClick, modifier, isEnabled, shape, CurrentPalette.invert(), content)

@Composable
expect fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    shape: Shape = CircleShape,
    palette: Palette/* = CurrentPalette.invert()*/,
    content: @Composable () -> Unit
)