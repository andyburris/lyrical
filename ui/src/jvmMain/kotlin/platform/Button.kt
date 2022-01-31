@file:JvmName("PlatformButton")
package platform

import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.ProvideTextStyle
import compose.multiplatform.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.shape.Shape
import compose.multiplatform.ui.shape.implementation
import org.jetbrains.compose.common.core.graphics.implementation
import org.jetbrains.compose.common.ui.implementation
import styles.implementation
import androidx.compose.material.Button as JButton

@Composable
actual fun Button(
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean,
    shape: Shape,
    buttonColors: Palette,
    content: @Composable () -> Unit,
) {
    JButton(
        onClick = onClick,
        modifier = modifier.implementation.height(56.dp),
        enabled = isEnabled,
        shape = shape.implementation,
        colors = lyricalButtonColors(buttonColors),
    ) {
        val enabledPalette = when(isEnabled) {
            true -> buttonColors
            false -> buttonColors.copy(contentPrimary = buttonColors.contentTernary, contentSecondary = buttonColors.contentTernary)
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