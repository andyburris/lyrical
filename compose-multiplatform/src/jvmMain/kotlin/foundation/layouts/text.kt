package compose.multiplatform.foundation

import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import compose.multiplatform.ui.Color
import compose.multiplatform.ui.text.TextOverflow
import compose.multiplatform.ui.text.TextStyle
import org.jetbrains.compose.common.core.graphics.implementation
import styles.implementation

@Composable
actual fun Text(
    text: String,
    modifier: Modifier,
    style: TextStyle?,
    color: Color?,
    maxLines: Int,
    overflow: TextOverflow,
) {
    androidx.compose.material.Text(
        text = text,
        modifier = modifier.implementation,
        style = style?.implementation ?: LocalTextStyle.current,
        color = color?.implementation ?: androidx.compose.ui.graphics.Color.Unspecified,
        maxLines = maxLines,
        overflow = overflow.implementation
    )
}
