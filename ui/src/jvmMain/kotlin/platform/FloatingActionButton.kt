@file:JvmName("PlatformFloatingActionButton")
package platform

import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.Shape
import jetbrains.compose.common.shapes.implementation
import org.jetbrains.compose.common.core.graphics.implementation
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.implementation

@Composable
actual fun ActualFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean,
    shape: Shape,
    palette: Palette,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.implementation,
        shape = shape.implementation,
        backgroundColor = palette.background.implementation,
        contentColor = palette.contentPrimary.implementation,
    ) {
        val enabledPalette = when(isEnabled) {
            true -> palette
            false -> palette.copy(contentPrimary = palette.contentTernary, contentSecondary = palette.contentTernary)
        }
        ProvidePalette(enabledPalette) {
            content()
        }
    }
}