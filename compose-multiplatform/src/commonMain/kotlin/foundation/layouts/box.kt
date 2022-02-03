package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.modifier.BoxScope

@Composable
expect fun Box(
    modifier: Modifier = Modifier.Companion,
    content: @Composable BoxScope.() -> Unit = {}
)
