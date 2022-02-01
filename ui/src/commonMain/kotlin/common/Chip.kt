package common

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Box
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.unit.dp
import org.jetbrains.compose.common.ui.draw.clip
import platform.CurrentPalette
import platform.LyricalTheme
import platform.invert

@Composable
fun Chip(text: String, modifier: Modifier = Modifier, selected: Boolean = false) { //TODO: add color schemes
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(if (selected) CurrentPalette.invert().background else LyricalTheme.colors.overlay)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = LyricalTheme.typography.subtitle1,
            color = if (selected) CurrentPalette.invert().contentPrimary else CurrentPalette.contentSecondary
        )
    }
}