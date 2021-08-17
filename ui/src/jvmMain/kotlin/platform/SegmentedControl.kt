@file:JvmName("PlatformSegmentedControl")
package platform

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.core.graphics.implementation
import org.jetbrains.compose.common.foundation.clickable
import org.jetbrains.compose.common.foundation.layout.fillMaxHeight
import org.jetbrains.compose.common.foundation.layout.width
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import org.jetbrains.compose.common.ui.size
import org.jetbrains.compose.common.ui.unit.dp
import org.jetbrains.compose.common.ui.unit.implementation
import java.awt.Color.*

@Composable
actual fun <T> ActualSegmentedControl(
    selected: T,
    options: List<T>,
    modifier: Modifier,
    stringify: T.() -> String,
    onSelect: (T) -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier.implementation
            .border(width = 1.dp.implementation, color = CurrentPalette.contentTernary.implementation, shape = CircleShape)
            .padding(4.dp.implementation)
    ) {
        val indicatorWidth = (this.maxWidth / 3).value.toInt().dp
        val offset = animateDpAsState(indicatorWidth.implementation * options.indexOf(selected)).value
        Box(androidx.compose.ui.Modifier.size(width = indicatorWidth.implementation, height = 32.dp.implementation).offset(offset).background(CurrentPalette.invert().background.implementation, shape = CircleShape))
        Row(androidx.compose.ui.Modifier.height(height = 32.dp.implementation)) {
            options.forEach { option ->
                val textColor = animateColorAsState(if (option == selected) CurrentPalette.invert().contentPrimary.implementation else CurrentPalette.contentSecondary.implementation).value.run {
                    Color((red * 255).toInt(), (blue * 255).toInt(), (green * 255).toInt())
                }
                Text(
                    text = option.stringify(),
                    modifier = Modifier
                        .fillMaxHeight(1f)
                        .width(indicatorWidth)//TODO: replace with Modifier.weight(1f)
                        .clickable { onSelect.invoke(option) },
                    style = LyricalTheme.typography.subtitle2,
                    color = textColor,
                )
            }
        }
    }
}