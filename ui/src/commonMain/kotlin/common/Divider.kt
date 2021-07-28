package common

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.size
import org.jetbrains.compose.common.ui.unit.dp

@Composable
fun Divider(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color)
            .fillMaxWidth()
            .size(1.dp)
    ) {}
}