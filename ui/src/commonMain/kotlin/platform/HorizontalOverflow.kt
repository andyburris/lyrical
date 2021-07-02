package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.foundation.layout.Arrangement
import org.jetbrains.compose.common.ui.Alignment
import org.jetbrains.compose.common.ui.Modifier

/**
 * Row that handles horizontal overflow based on platform
 * Mobile: horizontal scroll
 * Desktop: wrap items to next line
 */
@Composable
expect fun HorizontalOverflowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit
)