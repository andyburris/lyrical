package org.jetbrains.compose.common.foundation.layout

import org.jetbrains.compose.common.ui.Modifier
import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.ui.Alignment
import androidx.compose.foundation.layout.Column as JColumn
import org.jetbrains.compose.common.ui.implementation

@Composable
internal actual fun Column(
    modifier: Modifier,
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable () -> Unit
) {
    JColumn(modifier = modifier.implementation, verticalArrangement = verticalArrangement.implementation) {
        content.invoke()
    }
}