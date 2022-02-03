@file:JvmName("PlatformTextField")
package platform

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.Text
import compose.multiplatform.ui.Color
import org.jetbrains.compose.common.core.graphics.implementation
import compose.multiplatform.foundation.layout.Box
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.text.TextStyle
import org.jetbrains.compose.common.ui.implementation
import platform.keyboard.*
import styles.implementation

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
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.implementation,
        enabled = enabled,
        textStyle = (textStyle?.implementation ?: LocalTextStyle.current).copy(color = textColor?.implementation ?: LocalContentColor.current),
        decorationBox = { innerText ->
            Box {
                if (value.isEmpty()) {
                    Text(placeholder, style = textStyle, color = placeholderColor)
                }
                innerText()
            }
        },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions.implementation,
        keyboardActions = keyboardActions.implementation,
    )
}

val KeyboardActions.implementation: androidx.compose.foundation.text.KeyboardActions get() = androidx.compose.foundation.text.KeyboardActions(
    onDone = onDone?.let { { it.invoke(this.multiplatformImplementation) } },
    onGo = onGo?.let { { it.invoke(this.multiplatformImplementation) } },
    onNext = onNext?.let { { it.invoke(this.multiplatformImplementation) } },
    onPrevious = onPrevious?.let { { it.invoke(this.multiplatformImplementation) } },
    onSearch = onSearch?.let { { it.invoke(this.multiplatformImplementation) } },
    onSend = onSend?.let { { it.invoke(this.multiplatformImplementation) } },
)

val androidx.compose.foundation.text.KeyboardActionScope.multiplatformImplementation: KeyboardActionScope get() = object : KeyboardActionScope {
    override fun defaultKeyboardAction(imeAction: ImeAction) {
        this@multiplatformImplementation.defaultKeyboardAction(imeAction.implementation)
    }
}

val ImeAction.implementation: androidx.compose.ui.text.input.ImeAction get() = when(this) {
    ImeAction.None -> androidx.compose.ui.text.input.ImeAction.None
    ImeAction.Default -> androidx.compose.ui.text.input.ImeAction.Default
    ImeAction.Go -> androidx.compose.ui.text.input.ImeAction.Go
    ImeAction.Search -> androidx.compose.ui.text.input.ImeAction.Search
    ImeAction.Send -> androidx.compose.ui.text.input.ImeAction.Send
    ImeAction.Previous -> androidx.compose.ui.text.input.ImeAction.Previous
    ImeAction.Next -> androidx.compose.ui.text.input.ImeAction.Next
    ImeAction.Done -> androidx.compose.ui.text.input.ImeAction.Done
    else -> throw Error("Invalid ImeAction")
}

val KeyboardOptions.implementation: androidx.compose.foundation.text.KeyboardOptions get() = androidx.compose.foundation.text.KeyboardOptions(
    capitalization = capitalization.implementation,
    autoCorrect = autoCorrect,
    keyboardType = keyboardType.implementation,
    imeAction = imeAction.implementation,
)

val KeyboardCapitalization.implementation: androidx.compose.ui.text.input.KeyboardCapitalization get() = when(this) {
    KeyboardCapitalization.None -> androidx.compose.ui.text.input.KeyboardCapitalization.None
    KeyboardCapitalization.Characters -> androidx.compose.ui.text.input.KeyboardCapitalization.Characters
    KeyboardCapitalization.Words -> androidx.compose.ui.text.input.KeyboardCapitalization.Words
    KeyboardCapitalization.Sentences -> androidx.compose.ui.text.input.KeyboardCapitalization.Sentences
}

val KeyboardType.implementation: androidx.compose.ui.text.input.KeyboardType get() = when(this) {
    KeyboardType.Text -> androidx.compose.ui.text.input.KeyboardType.Text
    KeyboardType.Ascii -> androidx.compose.ui.text.input.KeyboardType.Ascii
    KeyboardType.Number -> androidx.compose.ui.text.input.KeyboardType.Number
    KeyboardType.Phone -> androidx.compose.ui.text.input.KeyboardType.Phone
    KeyboardType.Uri -> androidx.compose.ui.text.input.KeyboardType.Uri
    KeyboardType.Email -> androidx.compose.ui.text.input.KeyboardType.Email
    KeyboardType.Password -> androidx.compose.ui.text.input.KeyboardType.Password
    KeyboardType.NumberPassword -> androidx.compose.ui.text.input.KeyboardType.NumberPassword
}