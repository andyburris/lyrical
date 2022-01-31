package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.applyTextStyle
import compose.multiplatform.ui.Color
import compose.multiplatform.foundation.layout.Box
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.text.TextStyle
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.TextInput

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
) {
    Box(modifier) {
        TextInput(value) {
            onInput {
                onValueChange.invoke(it.value)
            }
            if (!enabled) disabled()
            placeholder(placeholder)
            style {
                if (textStyle != null) applyTextStyle(textStyle)
            }
        }
    }
}