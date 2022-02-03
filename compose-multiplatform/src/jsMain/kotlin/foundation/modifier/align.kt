package compose.multiplatform.foundation.modifier

import compose.multiplatform.foundation.modifiers.ColumnScope
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.web.css.flexGrow

actual interface BoxScope {
    actual fun Modifier.align(alignment: Alignment): Modifier
}

class BoxScopeImpl : BoxScope {
    override fun Modifier.align(alignment: Alignment) = this then StyleModifier {
        //TODO: figure out CSS alignment (requires flexbox but need some way to overlap, maybe multiple 100% size flexboxes?)
    }
}
