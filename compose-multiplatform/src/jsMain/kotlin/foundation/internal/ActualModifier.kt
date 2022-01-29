package org.jetbrains.compose.common.internal

import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.web.css.StyleBuilder
import org.jetbrains.compose.web.attributes.AttrsBuilder

class ActualModifier : Modifier.Element {
    val styleHandlers = mutableListOf<StyleBuilder.() -> Unit>()
    val attrHandlers = mutableListOf<AttrsBuilder<*>.() -> Unit>()

    fun add(builder: StyleBuilder.() -> Unit) {
        styleHandlers.add(builder)
    }

    fun addAttributeBuilder(builder: AttrsBuilder<*>.() -> Unit) {
        attrHandlers.add(builder)
    }
}

fun Modifier.castOrCreate(): ActualModifier = (this as? ActualModifier) ?: ActualModifier()

fun StyleModifier(builder: StyleBuilder.() -> Unit) = ActualModifier().apply { add(builder) }
fun AttributeModifier(builder: AttrsBuilder<*>.() -> Unit) = ActualModifier().apply { addAttributeBuilder(builder) }