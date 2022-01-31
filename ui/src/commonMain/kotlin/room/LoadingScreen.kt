package room

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.ui.Modifier
import platform.LyricalTheme

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