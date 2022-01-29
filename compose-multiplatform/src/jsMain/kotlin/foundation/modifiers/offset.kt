package org.jetbrains.compose.common.foundation.layout

import org.jetbrains.compose.common.internal.StyleModifier
import org.jetbrains.compose.common.ui.unit.Dp
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.px

actual fun Modifier.offset(x: Dp, y: Dp): Modifier = this then StyleModifier {
    property("transform", "translate(${x.value.px}, ${y.value.px})") //TODO: use transform builder functions when added
}
