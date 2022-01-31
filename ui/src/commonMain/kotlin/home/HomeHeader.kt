package home

import androidx.compose.runtime.Composable
import common.Icon
import compose.multiplatform.foundation.Image
import compose.multiplatform.foundation.Resource
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Box
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.unit.dp
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.size
import platform.LyricalTheme

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(resource = Resource.File("LyricalIcon.svg"), contentDescription = "Lyrical logo", modifier = Modifier.size(32.dp))
            Text("Lyrical", style = LyricalTheme.typography.h2)
        }
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