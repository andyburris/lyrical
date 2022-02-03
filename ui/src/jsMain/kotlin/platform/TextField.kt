package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.applyTextStyle
import compose.multiplatform.ui.Color
import compose.multiplatform.foundation.layout.Box
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.text.TextStyle
import org.jetbrains.compose.web.attributes.builders.InputAttrsBuilder
import org.jetbrains.compose.web.attributes.builders.TextAreaAttrsBuilder
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.StyleBuilder
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.TextInput
import org.jetbrains.compose.web.events.SyntheticInputEvent
import org.jetbrains.compose.web.events.SyntheticKeyboardEvent
import platform.keyboard.ImeAction
import platform.keyboard.KeyboardActionScope
import platform.keyboard.KeyboardActions
import platform.keyboard.KeyboardOptions

@Composable
actual fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    textStyle: TextStyle?,
    textColor: Color?,
    placeholder: String,
    placeholderColor: Color?,
    singleLine: Boolean,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
) {
    Box(modifier) {
        val builder: CommonInputBuilder.() -> Unit = {
            onInput {
                onValueChange.invoke(it.value)
            }
            if (!enabled) disabled()
            placeholder(placeholder)
            style {
                if (textStyle != null) applyTextStyle(textStyle)
            }
            onKeyDown {
                if (it.key == "Enter") {
                    keyboardActions.onGo?.invoke(object : KeyboardActionScope {
                        override fun defaultKeyboardAction(imeAction: ImeAction) {}
                    }) //TODO: cycle all options?
                }
            }
        }
        when(singleLine) {
            true -> TextInput(value, builder.toInputAttrsBuilder())
            false -> TextArea(value, builder.toTextAreaAttrsBuilder())
        }
    }
}

private interface CommonInputBuilder {
    fun onInput(listener: (SyntheticInputEvent<String, *>) -> Unit)
    fun disabled()
    fun placeholder(placeholder: String)
    fun style(builder: StyleBuilder.() -> Unit)
    fun onKeyDown(listener: (SyntheticKeyboardEvent) -> Unit)
}

private fun (CommonInputBuilder.() -> Unit).toInputAttrsBuilder(): InputAttrsBuilder<String>.() -> Unit = {
    val attrsBuilder = this
    val commonInputBuilderImpl = object : CommonInputBuilder {
        override fun onInput(listener: (SyntheticInputEvent<String, *>) -> Unit) = attrsBuilder.onInput(listener)
        override fun disabled() { attrsBuilder.disabled() }
        override fun placeholder(placeholder: String) { attrsBuilder.placeholder(placeholder) }
        override fun style(builder: StyleBuilder.() -> Unit) { attrsBuilder.style(builder) }
        override fun onKeyDown(listener: (SyntheticKeyboardEvent) -> Unit) { attrsBuilder.onKeyDown(listener) }
    }
    commonInputBuilderImpl.apply(this@toInputAttrsBuilder)
}

private fun (CommonInputBuilder.() -> Unit).toTextAreaAttrsBuilder(): TextAreaAttrsBuilder.() -> Unit = {
    val attrsBuilder = this
    val commonInputBuilderImpl = object : CommonInputBuilder {
        override fun onInput(listener: (SyntheticInputEvent<String, *>) -> Unit) = attrsBuilder.onInput(listener)
        override fun disabled() { attrsBuilder.disabled() }
        override fun placeholder(placeholder: String) { attrsBuilder.placeholder(placeholder) }
        override fun style(builder: StyleBuilder.() -> Unit) { attrsBuilder.style(builder) }
        override fun onKeyDown(listener: (SyntheticKeyboardEvent) -> Unit) { attrsBuilder.onKeyDown(listener) }
    }
    commonInputBuilderImpl.apply(this@toTextAreaAttrsBuilder)
}