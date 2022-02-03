package compose.multiplatform.foundation.modifiers

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.web.css.add
import org.jetbrains.compose.web.css.flexGrow

actual interface RowScope {
    actual fun Modifier.weight(fraction: Float, fill: Boolean): Modifier
}

class RowScopeImpl : RowScope {
    override fun Modifier.weight(fraction: Float, fill: Boolean) = this then StyleModifier {
        flexGrow(fraction)
    }
}

actual interface ColumnScope {
    actual fun Modifier.weight(fraction: Float, fill: Boolean): Modifier
}

class ColumnScopeImpl : ColumnScope {
    override fun Modifier.weight(fraction: Float, fill: Boolean) = this then StyleModifier {
        flexGrow(fraction)
    }
}
