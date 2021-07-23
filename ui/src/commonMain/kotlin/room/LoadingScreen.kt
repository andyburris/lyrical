package room

import androidx.compose.runtime.Composable
import platform.LyricalTheme
import platform.Text
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier

@Composable
fun LoadingScreen(loadingDescription: String, modifier: Modifier = Modifier) {
    Column(modifier) { //TODO: add Arrangement.SpacedBy(32.dp, Alignment.CenterVertically)
        Text("Loading...", Modifier.fillMaxWidth(), LyricalTheme.typography.h1)
        Column { //TODO: add Arrangement.SpacedBy(16.dp)
            //TODO: add ProgressIndicator
            Text(loadingDescription.uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackgroundSecondary)
        }
    }
}