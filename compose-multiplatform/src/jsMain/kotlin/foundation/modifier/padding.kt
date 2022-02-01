package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.internal.StyleModifier
import compose.multiplatform.ui.unit.Dp
import org.jetbrains.compose.web.css.*

actual fun Modifier.padding(start: Dp, top: Dp, end: Dp, bottom: Dp): Modifier = this then StyleModifier {
    // yes, it's not a typo, what Modifier.padding does is actually adding margin
    //TODO: add rtl support
    marginLeft(start.value.px)
    marginTop(top.value.px)
    marginRight(end.value.px)
    marginBottom(bottom.value.px)
}