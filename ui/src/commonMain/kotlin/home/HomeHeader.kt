package home

import androidx.compose.runtime.Composable
import common.Icon
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
import platform.Image
import platform.Resource

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth()) { //TODO: add Arrangement.SpaceBetween()
        Image(resource = Resource.File("LyricalIcon.svg"), contentDescription = "Lyrical logo", modifier = Modifier.size(32.dp))
        Text("Lyrical", style = LyricalTheme.typography.h2)
        ProfilePicture(modifier = Modifier.size(40.dp))
        //TODO: add popup
    }
}

@Composable
fun ProfilePicture(modifier: Modifier = Modifier) {
    Box(modifier.clip(CircleShape).background(LyricalTheme.colors.overlay)) {
        Icon(icon = Icon.Person, contentDescription = null) //TODO: add Modifier.align(Alignment.Center)
    }
}