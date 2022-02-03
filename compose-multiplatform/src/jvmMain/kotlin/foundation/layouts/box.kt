package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.modifier.BoxScope
import compose.multiplatform.foundation.modifier.toMultiplatformBoxScope
import androidx.compose.foundation.layout.Box as JBox

@Composable
actual fun Box(modifier: Modifier, content: @Composable BoxScope.() -> Unit) {
    JBox(modifier.implementation) {
        content.invoke(this.toMultiplatformBoxScope())
    }
}