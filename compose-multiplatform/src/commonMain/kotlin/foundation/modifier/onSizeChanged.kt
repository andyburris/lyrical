package org.jetbrains.compose.common.ui.layout

import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.IntSize

expect fun Modifier.onSizeChanged(
    onSizeChanged: (IntSize) -> Unit
): Modifier
