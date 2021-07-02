package platform

import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.CircleShape
import jetbrains.compose.common.shapes.Shape
import org.jetbrains.compose.common.ui.Modifier

@Composable
expect fun Button(onClick: () -> Unit, modifier: Modifier = Modifier, isEnabled: Boolean = true, shape: Shape = CircleShape, content: () -> Unit) //TODO: add ButtonElevation