package org.jetbrains.compose.common.ui

import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.unit.implementation
import org.jetbrains.compose.common.internal.castOrCreate
import androidx.compose.foundation.layout.padding
import compose.multiplatform.ui.Modifier

val Modifier.implementation
    get() = castOrCreate().modifier
