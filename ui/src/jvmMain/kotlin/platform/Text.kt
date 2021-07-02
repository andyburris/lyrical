package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.ui.Modifier
import styles.text.TextStyle

@Composable
actual fun Text(text: String, modifier: Modifier, style: TextStyle?, color: Color?) {
    androidx.compose.material.Text(text, )
}