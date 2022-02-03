package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Modifier

expect interface BoxScope {
    fun Modifier.align(alignment: Alignment): Modifier
}