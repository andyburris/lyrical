package org.jetbrains.compose.common.foundation.layout

import org.jetbrains.compose.common.ui.Modifier
import androidx.compose.runtime.Composable

@Composable
internal expect fun Box(
    modifier: Modifier = Modifier.Companion,
    content: @Composable () -> Unit
)
