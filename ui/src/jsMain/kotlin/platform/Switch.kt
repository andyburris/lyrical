package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.layout.Box
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.unit.dp
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.size

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