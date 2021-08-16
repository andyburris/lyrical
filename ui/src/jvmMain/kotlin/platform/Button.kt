@file:JvmName("PlatformButton")
package platform

import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jetbrains.compose.common.shapes.Shape
import jetbrains.compose.common.shapes.implementation
import org.jetbrains.compose.common.core.graphics.implementation
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import styles.implementation

@Composable
actual fun ActualButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean,
    shape: Shape,
    palette: Palette,
    content: @Composable () -> Unit
) {
    androidx.compose.material.Button(
        onClick = onClick,
        modifier = modifier.implementation.height(56.dp),
        enabled = isEnabled,
        shape = shape.implementation,
        colors = lyricalButtonColors(palette),
    ) {
        val enabledPalette = when(isEnabled) {
            true -> palette
            false -> palette.copy(contentPrimary = palette.contentTernary, contentSecondary = palette.contentTernary)
        }
        ProvidePalette(enabledPalette) {
            ProvideTextStyle(LyricalTheme.typography.subtitle1.implementation) {
                content()
            }
        }
    }
}

@Composable
private fun lyricalButtonColors(palette: Palette) = buttonColors(
    backgroundColor = palette.background.implementation,
    disabledBackgroundColor = palette.background.implementation,
    contentColor = palette.contentPrimary.implementation,
    disabledContentColor = palette.contentTernary.implementation
)