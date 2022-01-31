package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.shape.Shape
import compose.multiplatform.ui.Modifier

@Composable
actual fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean,
    shape: Shape,
    palette: Palette,
    content: @Composable () -> Unit
) {
    Button(modifier = modifier, onClick = onClick) {
        val enabledPalette = when(isEnabled) {
            true -> palette
            false -> palette.copy(contentPrimary = palette.contentTernary, contentSecondary = palette.contentTernary)
        }
        ProvidePalette(enabledPalette) {
            content()
        }
    }
}