package common

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Box
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.unit.dp
import org.jetbrains.compose.common.ui.draw.clip

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