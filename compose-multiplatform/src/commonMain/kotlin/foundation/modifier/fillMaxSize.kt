package compose.multiplatform.foundation.modifiers

import compose.multiplatform.foundation.layout.fillMaxHeight
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.ui.Modifier

fun Modifier.fillMaxSize(fraction: Float = 1f) = this.fillMaxWidth(fraction).fillMaxHeight(fraction)