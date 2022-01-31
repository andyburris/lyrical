
package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier

/**
 * Row that handles horizontal overflow based on platform
 * Mobile: horizontal scroll
 * Desktop: wrap items to next line
 */
@Composable
fun HorizontalOverflowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit
) = ActualHorizontalOverflowRow(modifier, horizontalArrangement, verticalAlignment, content)

/**
 * Row that handles horizontal overflow based on platform
 * Mobile: horizontal scroll
 * Desktop: wrap items to next line
 */
@Composable
expect fun ActualHorizontalOverflowRow(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable () -> Unit
)