package org.jetbrains.compose.common.internal

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.asAttributeBuilderApplier
import org.jetbrains.compose.web.dom.Div

@Composable
fun ModifierWrapperLayout(modifier: Modifier, content: @Composable () -> Unit) {
    println("ModifierWrapperLayout, modifier = $modifier, modifier length = ${modifier.foldOut(0) { _, acc -> acc + 1 }}")
    val wrappedContent: @Composable () -> Unit = modifier.foldOut(content) { element, innerContent ->
        println("folding element = $element")
        val wrapped: @Composable () -> Unit = {
            Div(attrs = element.asAttributeBuilderApplier() ) {
                innerContent()
            }
        }
        wrapped
    }
    wrappedContent()
}

@Composable
fun modifierWrapper(modifier: Modifier, content: @Composable () -> Unit) {
    ModifierWrapperLayout(modifier) {
        content()
    }
}