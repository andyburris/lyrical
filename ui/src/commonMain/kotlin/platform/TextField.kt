package platform

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Color
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.text.TextStyle

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
    BasicTextField(
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
expect fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle? = null,
    textColor: Color? = null,
    placeholder: String = "",
    placeholderColor: Color? = null,
)