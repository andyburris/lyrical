package common

import androidx.compose.runtime.Composable
import jetbrains.compose.common.shapes.CircleShape
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp
import platform.CurrentPalette
import platform.Text

@Composable
fun Chip(text: String, modifier: Modifier = Modifier, selected: Boolean = false) { //TODO: add color schemes
    Box(
        modifier = modifier
            .clip(CircleShape)
            //.background(if (selected) CurrentPalette.)
            .padding(12.dp) //TODO: change vertical padding to 4.dp
    ) {
        Text(text) //TODO: change text style?
    }
}