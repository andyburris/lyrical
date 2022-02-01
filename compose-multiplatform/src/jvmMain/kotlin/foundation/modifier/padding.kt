@file:JvmName("PlatformPadding")

package compose.multiplatform.foundation.modifier

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.unit.implementation
import org.jetbrains.compose.common.internal.castOrCreate

@Composable
actual fun Modifier.padding(start: Dp, top: Dp, end: Dp, bottom: Dp): Modifier = castOrCreate().apply {
    modifier = modifier.padding(
        start = start.implementation,
        top = top.implementation,
        end = end.implementation,
        bottom = bottom.implementation
    )
}