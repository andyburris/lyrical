package common

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.layout.Box
import compose.multiplatform.foundation.layout.fillMaxWidth
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.ui.Color
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import org.jetbrains.compose.common.ui.size

@Composable
fun Divider(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color)
            .fillMaxWidth()
            .size(1.dp)
    ) {}
}