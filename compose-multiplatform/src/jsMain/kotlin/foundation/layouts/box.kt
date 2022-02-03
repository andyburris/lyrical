package compose.multiplatform.foundation.layout

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.modifier.BoxScope
import compose.multiplatform.foundation.modifier.BoxScopeImpl
import org.jetbrains.compose.common.internal.modifierWrapper
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.dom.Div

@Composable
actual fun Box(modifier: Modifier, content: @Composable BoxScope.() -> Unit) = modifierWrapper(modifier) {
    Div() {
        content(BoxScopeImpl())
    }
}
