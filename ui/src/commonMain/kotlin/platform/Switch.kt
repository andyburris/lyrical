package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Modifier

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    palette: Palette = CurrentPalette.invert(),
) = ActualSwitch(checked, onCheckedChange, modifier, enabled, palette)

@Composable
expect fun ActualSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
    palette: Palette,
)