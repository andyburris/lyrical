package compose.multiplatform.foundation.modifier

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Modifier

@Composable
expect fun Modifier.verticalScroll(): Modifier

@Composable
expect fun Modifier.horizontalScroll(): Modifier