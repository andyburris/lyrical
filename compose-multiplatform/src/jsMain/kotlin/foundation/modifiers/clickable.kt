package compose.multiplatform.foundation.modifier

import org.jetbrains.compose.common.internal.AttributeModifier
import compose.multiplatform.ui.Modifier

actual fun Modifier.clickable(onClick: () -> Unit): Modifier = this then AttributeModifier {
    onClick { onClick() }
}
