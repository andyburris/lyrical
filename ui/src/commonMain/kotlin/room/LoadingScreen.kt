package room

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.foundation.modifiers.fillMaxSize
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import platform.LyricalTheme

@Composable
fun LoadingScreen(loadingDescription: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp, alignment = Alignment.CenterVertically),
    ) {
        Text("Loading...", Modifier.fillMaxWidth(), LyricalTheme.typography.h1)
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            //TODO: add ProgressIndicator
            Text(
                text = loadingDescription.uppercase(),
                style = LyricalTheme.typography.subtitle1,
                color = LyricalTheme.colors.onBackgroundSecondary
            )
        }
    }
}