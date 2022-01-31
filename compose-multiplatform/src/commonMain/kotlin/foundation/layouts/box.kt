package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import androidx.compose.runtime.Composable

@Composable
expect fun Box(
    modifier: Modifier = Modifier.Companion,
    content: @Composable () -> Unit = {}
)
