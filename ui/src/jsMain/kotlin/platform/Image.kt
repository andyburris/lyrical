package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.asAttributeBuilderApplier
import org.jetbrains.compose.web.dom.Img

@Composable
actual fun ActualImage(
    uri: String,
    contentDescription: String?,
    modifier: Modifier,
    placeholderURI: String?
) {
    Img(
        src = uri,
        alt = contentDescription ?: "",
        attrs = modifier.asAttributeBuilderApplier())
}