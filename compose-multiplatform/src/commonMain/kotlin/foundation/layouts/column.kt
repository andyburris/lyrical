package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Alignment

@Composable
expect fun Column(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit
)
