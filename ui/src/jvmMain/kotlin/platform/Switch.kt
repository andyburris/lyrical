@file:JvmName("PlatformSwitch")
package platform

import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.implementation
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.ui.implementation

@Composable
actual fun ActualSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
    palette: Palette,
) {
    androidx.compose.material.Switch(
        checked,
        onCheckedChange,
        modifier = modifier.implementation,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = palette.background.implementation,
            checkedTrackColor = palette.background.implementation,
            uncheckedThumbColor = palette.invert().background.implementation,
            uncheckedTrackColor = palette.invert().contentTernary.implementation,
            disabledCheckedThumbColor = palette.background.implementation,
            disabledCheckedTrackColor = palette.background.implementation,
            disabledUncheckedThumbColor = palette.invert().background.implementation,
            disabledUncheckedTrackColor = palette.invert().contentTernary.implementation,
        )
    )
}