package compose.multiplatform.foundation.modifiers

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate

actual interface RowScope {
    actual fun Modifier.weight(fraction: Float, fill: Boolean): Modifier
}

fun androidx.compose.foundation.layout.RowScope.toMultiplatformRowScope(): RowScope = object : RowScope {
    override fun Modifier.weight(fraction: Float, fill: Boolean): Modifier = castOrCreate().apply {
        modifier = modifier.weight(fraction, fill)
    }
}

actual interface ColumnScope {
    actual fun Modifier.weight(fraction: Float, fill: Boolean): Modifier
}

fun androidx.compose.foundation.layout.ColumnScope.toMultiplatformColumnScope(): ColumnScope = object : ColumnScope {
    override fun Modifier.weight(fraction: Float, fill: Boolean): Modifier = castOrCreate().apply {
        modifier = modifier.weight(fraction, fill)
    }
}