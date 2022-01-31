@file:JvmName("PlatformHorizontalOverflow")
package platform

import FlowRow
import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.foundation.layout.implementation
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.ui.implementation

@Composable
actual fun ActualHorizontalOverflowRow(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable () -> Unit
) {
    FlowRow(
        modifier = modifier.implementation,
        mainAxisAlignment = when(horizontalArrangement.implementation) {
            androidx.compose.foundation.layout.Arrangement.Start -> MainAxisAlignment.Start
            androidx.compose.foundation.layout.Arrangement.Center -> MainAxisAlignment.Center
            androidx.compose.foundation.layout.Arrangement.End -> MainAxisAlignment.End
            androidx.compose.foundation.layout.Arrangement.SpaceAround -> MainAxisAlignment.SpaceAround
            androidx.compose.foundation.layout.Arrangement.SpaceBetween -> MainAxisAlignment.SpaceBetween
            androidx.compose.foundation.layout.Arrangement.SpaceEvenly -> MainAxisAlignment.SpaceEvenly
            else -> MainAxisAlignment.Start
        },
        mainAxisSpacing = horizontalArrangement.implementation.spacing,
        content = content
    )
}
