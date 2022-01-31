package org.jetbrains.compose.common.ui.draw

import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.Shape

expect fun Modifier.clip(shape: Shape): Modifier
