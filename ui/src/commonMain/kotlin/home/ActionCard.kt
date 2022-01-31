package home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import common.Icon
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import org.jetbrains.compose.common.ui.draw.clip
import platform.CurrentPalette
import platform.LyricalTheme
import platform.Palette

@Composable
fun ActionCard(title: String, summary: String, icon: Icon, palette: Palette = CurrentPalette, modifier: Modifier = Modifier) {
    SideEffect {
        println("card title = $title, palette = $palette")
    }
    Column(
        modifier = modifier.background(palette.backgroundLight).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(icon = icon, contentDescription = null, tint = palette.contentPrimary)
        Column {
            Text(title, style = LyricalTheme.typography.subtitle1, color = palette.contentPrimary)
            Text(summary, style = LyricalTheme.typography.body1, color = palette.contentSecondary)
        }
    }
}