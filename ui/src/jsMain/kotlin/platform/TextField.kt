package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.TextInput
import styles.text.TextStyle

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
actual fun ActualBaseTextField(
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