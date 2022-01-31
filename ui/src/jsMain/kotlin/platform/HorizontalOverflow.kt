package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Modifier

/**
 * Row that handles horizontal overflow based on platform
 * Mobile: horizontal scroll
 * Desktop: wrap items to next line
 */
@Composable
actual fun ActualHorizontalOverflowRow(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable () -> Unit
) {
   Row(modifier, horizontalArrangement, verticalAlignment, content) //TODO: wrap overflow
}