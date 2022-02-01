package compose.multiplatform.foundation.modifiers

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.web.css.flexGrow

actual interface RowScope {
    actual fun Modifier.weight(fraction: Float, fill: Boolean): Modifier
}

class RowScopeImpl : RowScope {
    override fun Modifier.weight(fraction: Float, fill: Boolean) = castOrCreate().apply {
        add {
            flexGrow(fraction)
        }
    }
}
