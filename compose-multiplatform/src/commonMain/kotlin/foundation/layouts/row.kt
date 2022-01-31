package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Alignment

@Composable
expect fun Row(
    modifier: Modifier = Modifier.Companion,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit
)
