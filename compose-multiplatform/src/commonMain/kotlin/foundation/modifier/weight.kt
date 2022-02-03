package compose.multiplatform.foundation.modifiers

import compose.multiplatform.ui.Modifier

expect interface RowScope {
    fun Modifier.weight(fraction: Float, fill: Boolean = true): Modifier
}

expect interface ColumnScope {
    fun Modifier.weight(fraction: Float, fill: Boolean = true): Modifier
}