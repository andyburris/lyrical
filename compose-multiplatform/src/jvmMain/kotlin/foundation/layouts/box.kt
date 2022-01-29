package org.jetbrains.compose.common.foundation.layout

import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box as JBox

@Composable
internal actual fun Box(modifier: Modifier, content: @Composable () -> Unit) {
    JBox(modifier.implementation) {
        content.invoke()
    }
}