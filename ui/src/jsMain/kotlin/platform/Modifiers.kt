package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.web.css.flexGrow
import org.jetbrains.compose.web.css.overflowY

actual fun Modifier.weight(weight: Float): Modifier = castOrCreate().apply {
    add {
        flexGrow(weight)
    }
}

@Composable
actual fun Modifier.verticalScroll(): Modifier = castOrCreate().apply {
    add {
        overflowY("scroll")
    }
}