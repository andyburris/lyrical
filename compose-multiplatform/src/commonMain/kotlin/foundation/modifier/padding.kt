package compose.multiplatform.foundation.modifier

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.unit.dp

@Composable fun Modifier.padding(all: Dp): Modifier = padding(all, all)
@Composable fun Modifier.padding(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) = padding(horizontal, vertical, horizontal, vertical)
@Composable expect fun Modifier.padding(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp): Modifier