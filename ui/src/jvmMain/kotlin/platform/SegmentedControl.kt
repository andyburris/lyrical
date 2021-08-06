@file:JvmName("PlatformSegmentedControl")
package platform

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.CircleShape
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.core.graphics.implementation
import org.jetbrains.compose.common.foundation.border
import org.jetbrains.compose.common.foundation.clickable
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.implementation
import org.jetbrains.compose.common.ui.padding
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
        val indicatorWidth = this.maxWidth / 3
        val offset = animateDpAsState(indicatorWidth * options.indexOf(selected)).value
        Box(androidx.compose.ui.Modifier.size(width = indicatorWidth, height = 40.dp.implementation).offset(offset).background(LyricalTheme.colors.primary.implementation, shape = CircleShape))
        Row {
            options.forEach { option ->
                val textColor = animateColorAsState(if (option == selected) LyricalTheme.colors.onPrimary.implementation else LyricalTheme.colors.onBackgroundSecondary.implementation).value.run {
                    Color(red.toInt(), blue.toInt(), green.toInt())
                }
                Text(
                    text = option.stringify(),
                    modifier = Modifier.weight(1f).clickable { onSelect.invoke(option) },
                    style = LyricalTheme.typography.subtitle2,
                    color = textColor,
                )
            }
        }
    }
}