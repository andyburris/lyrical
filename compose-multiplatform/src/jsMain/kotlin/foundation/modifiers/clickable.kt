package org.jetbrains.compose.common.foundation

import org.jetbrains.compose.common.internal.AttributeModifier
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.internal.castOrCreate

actual fun Modifier.clickable(onClick: () -> Unit): Modifier = this then AttributeModifier {
    onClick { onClick() }
}
