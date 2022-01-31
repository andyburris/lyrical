package compose.multiplatform.ui

import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.common.internal.castOrCreate
import compose.multiplatform.ui.unit.Dp
import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.px

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
