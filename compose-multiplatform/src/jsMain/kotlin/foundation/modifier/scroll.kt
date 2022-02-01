package compose.multiplatform.foundation.modifier

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.css.overflowX
import org.jetbrains.compose.web.css.overflowY

@Composable
actual fun Modifier.verticalScroll(): Modifier = this then StyleModifier {
   overflowY("scroll")
}

@Composable
actual fun Modifier.horizontalScroll(): Modifier = this then StyleModifier {
    overflowX("scroll")
}