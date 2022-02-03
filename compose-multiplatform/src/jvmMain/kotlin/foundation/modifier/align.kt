package compose.multiplatform.foundation.modifier

import compose.multiplatform.foundation.modifiers.RowScope
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.ui.implementation

actual interface BoxScope {
    actual fun Modifier.align(alignment: Alignment): Modifier
}

fun androidx.compose.foundation.layout.BoxScope.toMultiplatformBoxScope(): BoxScope = object : BoxScope {
    override fun Modifier.align(alignment: Alignment): Modifier = castOrCreate().apply {
        modifier = modifier.align(alignment.implementation)
    }
}
