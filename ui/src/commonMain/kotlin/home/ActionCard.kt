package home

import androidx.compose.runtime.Composable
import platform.LyricalTheme
import platform.Text
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp

@Composable
fun ActionCard(title: String, summary: String, modifier: Modifier = Modifier) {
    Column(modifier.padding(32.dp)) {
        //TODO: Icon
        Text(title, Modifier.fillMaxWidth(), LyricalTheme.typography.subtitle1)
        Text(summary, Modifier.fillMaxWidth(), LyricalTheme.typography.body1)
    }
}