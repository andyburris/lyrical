
package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.foundation.layout.Arrangement
import org.jetbrains.compose.common.ui.Alignment
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier

/**
 * Row that handles horizontal overflow based on platform
 * Mobile: horizontal scroll
 * Desktop: wrap items to next line
 */
@OptIn(ExperimentalComposeWebWidgetsApi::class)
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
@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
expect fun ActualHorizontalOverflowRow(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable () -> Unit
)