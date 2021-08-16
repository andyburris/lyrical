package platform

import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.Shape
import org.jetbrains.compose.common.material.ButtonActual
import org.jetbrains.compose.common.ui.Modifier

@Composable
actual fun ActualFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean,
    shape: Shape,
    palette: Palette,
    content: @Composable () -> Unit
) {
    ButtonActual(modifier = modifier, onClick = onClick) {
        val enabledPalette = when(isEnabled) {
            true -> palette
            false -> palette.copy(contentPrimary = palette.contentTernary, contentSecondary = palette.contentTernary)
        }
        ProvidePalette(enabledPalette) {
            content()
        }
    }
}