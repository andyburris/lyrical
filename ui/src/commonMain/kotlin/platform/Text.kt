package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.ui.Modifier
import styles.text.TextStyle

@Composable
fun Text(text: String, modifier: Modifier = Modifier, style: TextStyle? = null, color: Color? = null) = ActualText(text, modifier, style, color)

@Composable
expect fun ActualText(text: String, modifier: Modifier, style: TextStyle?, color: Color?)

//TODO: remove wrapper method once multiplatform compose supports default parameters (see https://github.com/JetBrains/compose-jb/issues/758 and https://youtrack.jetbrains.com/issue/KT-44499)