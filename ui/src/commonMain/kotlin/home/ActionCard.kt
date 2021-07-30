package home

import androidx.compose.runtime.Composable
import platform.LyricalTheme
import platform.Text
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp
import platform.CurrentPalette
import platform.Palette

@Composable
fun ActionCard(title: String, summary: String, palette: Palette = CurrentPalette, modifier: Modifier = Modifier) {
    Column(modifier.background(palette.backgroundLight).padding(32.dp)) {
        //TODO: Icon
        Column {
            Text(title, style = LyricalTheme.typography.subtitle1, color = palette.contentPrimary)
            Text(summary, style = LyricalTheme.typography.body1, color = palette.contentSecondary)
        }
    }
}