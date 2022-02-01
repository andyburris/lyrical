package common

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.foundation.modifier.clickable
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import platform.CurrentPalette
import platform.LyricalTheme

@Composable
fun AppBar(
    title: String,
    subtitle: String? = null,
    icon: Icon = Icon.Clear,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    widget: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon = icon,
                contentDescription = "Navigate back",
                modifier = Modifier.clickable(onNavigateBack)
            )
            Column() {
                Text(title, style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentPrimary)
                if (subtitle != null) Text(subtitle, style = LyricalTheme.typography.caption, color = CurrentPalette.contentSecondary)
            }
        }
        if (widget != null) {
            widget()
        }
    }
}