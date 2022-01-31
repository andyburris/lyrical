package compose.multiplatform.foundation.layout

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.internal.modifierWrapper
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.dom.Div

@Composable
actual fun Box(modifier: Modifier, content: @Composable () -> Unit) = modifierWrapper(modifier) {
    Div() {
        content()
    }
}
