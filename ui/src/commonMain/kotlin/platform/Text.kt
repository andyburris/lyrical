package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.ui.Modifier
import styles.text.TextStyle

@Composable
expect fun Text(text: String, modifier: Modifier = Modifier, style: TextStyle? = null, color: Color? = null)