package compose.multiplatform.foundation

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.Color
import compose.multiplatform.ui.text.TextOverflow
import compose.multiplatform.ui.text.TextStyle

@Composable
expect fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    color: Color? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
)
