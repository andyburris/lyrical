package platform

import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.web.css.flexGrow

actual fun Modifier.weight(weight: Float): Modifier = castOrCreate().apply {
    add {
        flexGrow(weight)
    }
}