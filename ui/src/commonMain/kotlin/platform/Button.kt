package platform

import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.CircleShape
import jetbrains.compose.common.shapes.Shape
import org.jetbrains.compose.common.ui.Modifier

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    shape: Shape = CircleShape,
    palette: Palette = CurrentPalette.invert(),
    content: @Composable () -> Unit
) = ActualButton(onClick, modifier, isEnabled, shape, palette, content)

@Composable
expect fun ActualButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean,
    shape: Shape,
    palette: Palette,
    content: @Composable () -> Unit,
    //TODO: add ButtonElevation
)