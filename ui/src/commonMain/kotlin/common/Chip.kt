package common

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp
import platform.Text

@Composable
fun Chip(text: String, modifier: Modifier = Modifier) { //TODO: add color schemes
    Box(modifier.padding(12.dp)) { //TODO: change vertical padding to 4.dp
        Text(text) //TODO: change text style?
    }
}