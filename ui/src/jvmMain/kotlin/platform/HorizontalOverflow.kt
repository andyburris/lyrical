@file:JvmName("PlatformHorizontalOverflow")
package platform

import FlowRow
import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.foundation.layout.implementation
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.unit.implementation
import org.jetbrains.compose.common.ui.implementation

@Composable
actual fun HorizontalOverflowRow(
    modifier: Modifier,
    mainAxisAlignment: Alignment.Horizontal,
    mainAxisSpacing: Dp,
    crossAxisAlignment: Alignment.Vertical,
    crossAxisSpacing: Dp,
    content: @Composable () -> Unit
) {
    FlowRow(
        modifier = modifier.implementation,
        mainAxisAlignment = when(mainAxisAlignment) {
            Alignment.Start -> MainAxisAlignment.Start
            Alignment.CenterHorizontally -> MainAxisAlignment.Center
            Alignment.End -> MainAxisAlignment.End
            else -> MainAxisAlignment.Start
        },
        mainAxisSpacing = mainAxisSpacing.implementation,
        crossAxisAlignment = when(crossAxisAlignment.implementation) {
            Alignment.Top -> FlowCrossAxisAlignment.Start
            Alignment.CenterVertically -> FlowCrossAxisAlignment.Center
            Alignment.Bottom -> FlowCrossAxisAlignment.End
            else -> FlowCrossAxisAlignment.Start
        },
        crossAxisSpacing = crossAxisSpacing.implementation,
        content = content
    )
}
