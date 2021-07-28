package platform

import androidx.compose.runtime.Composable
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
    placeholder: String = "",
) {
    BaseTextField(value, onValueChange, modifier, enabled, textStyle = textStyle, placeholder = placeholder)
}

@Composable
fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle? = null,
    placeholder: String = "",
) = ActualBaseTextField(value, onValueChange, modifier, enabled, textStyle, placeholder)

@Composable
expect fun ActualBaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    textStyle: TextStyle?,
    placeholder: String,
)