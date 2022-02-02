
package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.unit.dp

/**
 * Row that handles horizontal overflow based on platform
 * Mobile: horizontal scroll
 * Desktop: wrap items to next line
 */
@Composable
expect fun HorizontalOverflowRow(
    modifier: Modifier = Modifier,
    mainAxisAlignment: Alignment.Horizontal = Alignment.Start,
    mainAxisSpacing: Dp = 0.dp,
    crossAxisAlignment: Alignment.Vertical = Alignment.Top,
    crossAxisSpacing: Dp = 0.dp,
    content: @Composable () -> Unit
)