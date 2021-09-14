@file:JvmName("PlatformTextField")
package platform

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.core.graphics.implementation
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import styles.implementation
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
        }
    )
}