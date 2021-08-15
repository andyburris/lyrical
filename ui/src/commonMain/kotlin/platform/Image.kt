package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.ui.Modifier

@Composable
fun Image(
    uri: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholderURI: String? = null
) = ActualImage(uri, contentDescription, modifier, placeholderURI)

@Composable
expect fun ActualImage(
    uri: String,
    contentDescription: String?,
    modifier: Modifier,
    placeholderURI: String?
)