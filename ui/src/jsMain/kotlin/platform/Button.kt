package platform

import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.Shape
import org.jetbrains.compose.common.material.ButtonActual
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
actual fun ActualButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean,
    shape: Shape,
    palette: Palette,
    content: @Composable () -> Unit
) {
    val enabledPalette = when(isEnabled) {
        true -> palette
        false -> palette.copy(contentPrimary = palette.contentTernary, contentSecondary = palette.contentTernary)
    }
    ButtonActual(modifier = modifier, onClick = onClick, content = content)
}