package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.ui.Modifier
import styles.text.TextStyle

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle? = null,
    textColor: Color? = CurrentPalette.contentPrimary,
    placeholder: String = "",
    placeholderColor: Color? = CurrentPalette.contentTernary,
) {
    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        textStyle = textStyle,
        textColor = textColor,
        placeholder = placeholder,
        placeholderColor = placeholderColor
    )
}

@Composable
fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle? = null,
    textColor: Color? = null,
    placeholder: String = "",
    placeholderColor: Color? = null,
) = ActualBaseTextField(value, onValueChange, modifier, enabled, textStyle, textColor, placeholder, placeholderColor)

@Composable
expect fun ActualBaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    textStyle: TextStyle?,
    textColor: Color? = null,
    placeholder: String,
    placeholderColor: Color? = null,
)