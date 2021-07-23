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
    content: @Composable () -> Unit
) = ActualButton(onClick, modifier, isEnabled, shape, content)

@Composable
expect fun ActualButton(onClick: () -> Unit, modifier: Modifier = Modifier, isEnabled: Boolean = true, shape: Shape = CircleShape, content: @Composable () -> Unit) //TODO: add ButtonElevation