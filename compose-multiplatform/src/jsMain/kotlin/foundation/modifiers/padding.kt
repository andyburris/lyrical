package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.unit.Dp
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.px

actual fun Modifier.padding(all: Dp): Modifier = this then StyleModifier {
    // yes, it's not a typo, what Modifier.padding does is actually adding margin
    margin(all.value.px)
}