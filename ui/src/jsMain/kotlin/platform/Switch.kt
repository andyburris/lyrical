package platform

import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.CircleShape
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.size
import org.jetbrains.compose.common.ui.unit.dp

@Composable
actual fun ActualSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
    palette: Palette,
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (checked) palette.background else palette.invert().background)
            .size(width = 32.dp, height = 20.dp)
    ) {
        Box(
            modifier = Modifier
                //TODO: add Modifier.align()
                .background(if (checked) palette.contentPrimary else palette.invert().contentPrimary)
                .size(16.dp)
        ) {}
    }
}