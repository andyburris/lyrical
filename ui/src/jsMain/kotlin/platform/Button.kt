package platform

import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.Shape
import org.jetbrains.compose.common.material.ButtonActual
import org.jetbrains.compose.common.ui.Modifier

@Composable
actual fun ActualButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean,
    shape: Shape,
    palette: Palette,
    content: @Composable () -> Unit
) {
    ButtonActual(modifier = modifier, onClick = onClick, content = content)
}