package org.jetbrains.compose.common.ui

import org.jetbrains.compose.common.ui.unit.Dp
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.internal.ActualModifier
import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.web.css.Color.RGB
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.css.*

fun Modifier.asAttributeBuilderApplier(
    passThroughHandler: (AttrsBuilder<*>.() -> Unit)? = null
): AttrsBuilder<*>.() -> Unit =
    castOrCreate().let { modifier ->
        val st: AttrsBuilder<*>.() -> Unit = {
            modifier.attrHandlers.forEach { it.invoke(this) }

            style {
                display(DisplayStyle.LegacyInlineFlex) //inline-flex behaves like traditional Compose layouts, so begin with that
                modifier.styleHandlers.forEach { it.invoke(this) }
            }

            passThroughHandler?.invoke(this)
        }

        st
    }

actual fun Modifier.padding(all: Dp): Modifier = this then StyleModifier {
    // yes, it's not a typo, what Modifier.padding does is actually adding margin
    margin(all.value.px)
}
