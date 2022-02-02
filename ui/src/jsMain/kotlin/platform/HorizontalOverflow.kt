package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.unit.dp

/**
 * Row that handles horizontal overflow based on platform
 * Mobile: horizontal scroll
 * Desktop: wrap items to next line
 */
@Composable
actual fun HorizontalOverflowRow(
    modifier: Modifier,
    mainAxisAlignment: Alignment.Horizontal,
    mainAxisSpacing: Dp,
    crossAxisAlignment: Alignment.Vertical,
    crossAxisSpacing: Dp,
    content: @Composable () -> Unit
) {
   Row(modifier, Arrangement.spacedBy(mainAxisSpacing, alignment = mainAxisAlignment), crossAxisAlignment) {
       content()
   } //TODO: wrap overflow
}