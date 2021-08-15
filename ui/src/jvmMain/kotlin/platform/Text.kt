@file:JvmName("PlatformText")
package platform

import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.core.graphics.implementation
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import styles.implementation
import styles.text.TextOverflow
import styles.text.TextStyle

@Composable
actual fun ActualText(
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