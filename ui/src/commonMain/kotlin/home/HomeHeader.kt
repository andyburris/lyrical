package home

import androidx.compose.runtime.Composable
import platform.LyricalTheme
import platform.Text
import jetbrains.compose.common.shapes.CircleShape
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.size
import org.jetbrains.compose.common.ui.unit.dp

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth()) { //TODO: add Arrangement.SpaceBetween()
        //TODO: add Lyrical logo
        Text("Lyrical", style = LyricalTheme.typography.h2)
        ProfilePicture(modifier = Modifier.size(40.dp))
        //TODO: add profile picture and popup
    }
}

@Composable
fun ProfilePicture(modifier: Modifier = Modifier) {
    Box(modifier.clip(CircleShape).background(LyricalTheme.colors.overlay)) {}
}