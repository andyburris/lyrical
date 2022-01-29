package org.jetbrains.compose.common.foundation.layout

import org.jetbrains.compose.common.ui.Modifier
import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.internal.ModifierWrapperLayout
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.common.ui.asAttributeBuilderApplier

@Composable
internal actual fun Box(modifier: Modifier, content: @Composable () -> Unit) {
    ModifierWrapperLayout(modifier) {
        Div(
            attrs = {}
        ) {
            content()
        }
    }
}
