package common

import androidx.compose.runtime.Composable
import platform.LyricalTheme
import platform.Text
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.ui.Modifier

@Composable
fun AppBar(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    widget: (@Composable () -> Unit)? = null
) {
    Row(modifier) {
        //TODO: Add navigation icon
        Column(/*TODO: add Modifier.weight(1f)*/) {
            Text(title, style = LyricalTheme.typography.subtitle1)
            if (subtitle != null) Text(subtitle, style = LyricalTheme.typography.caption)
        }
        if (widget != null) {
            widget()
        }
    }
}