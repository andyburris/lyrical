package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import styles.text.TextOverflow
import styles.text.TextStyle

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    color: Color? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) = ActualText(text, modifier, style, color, maxLines, overflow)

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
expect fun ActualText(
    text: String,
    modifier: Modifier,
    style: TextStyle?,
    color: Color?,
    maxLines: Int,
    overflow: TextOverflow,
)

//TODO: remove wrapper method once multiplatform compose supports default parameters (see https://github.com/JetBrains/compose-jb/issues/758 and https://youtrack.jetbrains.com/issue/KT-44499)