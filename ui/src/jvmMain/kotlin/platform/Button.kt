@file:JvmName("PlatformButton")
package platform

import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.Shape
import jetbrains.compose.common.shapes.implementation
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.implementation

@Composable
actual fun ActualButton(onClick: () -> Unit, modifier: Modifier, isEnabled: Boolean, shape: Shape, content: @Composable () -> Unit) {
    androidx.compose.material.Button(
        onClick = onClick,
        modifier = modifier.implementation,
        enabled = isEnabled,
        shape = shape.implementation,
        content = { content() }
    )
}