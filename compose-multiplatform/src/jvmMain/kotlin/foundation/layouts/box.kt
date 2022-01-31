package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box as JBox

@Composable
actual fun Box(modifier: Modifier, content: @Composable () -> Unit) {
    JBox(modifier.implementation) {
        content.invoke()
    }
}