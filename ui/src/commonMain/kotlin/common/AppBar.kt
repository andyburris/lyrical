package common

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.foundation.clickable
import platform.LyricalTheme
import platform.Text
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.ui.Alignment
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import platform.CurrentPalette
import platform.LocalPalette

@OptIn(ExperimentalComposeWebWidgetsApi::class)
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
        //TODO: add Arrangement.SpaceBetween
    ) {
        Icon(
            icon = icon,
            contentDescription = "Navigate back",
            modifier = Modifier.clickable(onNavigateBack)
        )
        Column(/*TODO: add Modifier.weight(1f)*/) {
            Text(title, style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentPrimary)
            if (subtitle != null) Text(subtitle, style = LyricalTheme.typography.caption, color = CurrentPalette.contentSecondary)
        }
        if (widget != null) {
            widget()
        }
    }
}