package platform

import compose.multiplatform.ui.Modifier
import androidx.compose.runtime.Composable
import compose.multiplatform.ui.shape.Shape
import org.jetbrains.compose.web.dom.Button

@Composable
actual fun Button(
    onClick: () -> Unit,
    modifier: Modifier,
    isEnabled: Boolean,
    shape: Shape,
    buttonColors: Palette,
    content: @Composable () -> Unit,
) {
    Button(
        attrs = {
            onClick { onClick() }
        }
    ) {
        content()
    }
    //TODO: convert to Box-based button
}
