package platform

import FlowRow
import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.foundation.layout.Arrangement
import org.jetbrains.compose.common.foundation.layout.implementation
import org.jetbrains.compose.common.ui.Alignment
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.implementation

@Composable
actual fun HorizontalOverflowRow(
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
        mainAxisSpacing = horizontalArrangement.implementation.spacing
    )
}
