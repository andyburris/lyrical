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