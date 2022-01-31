package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Alignment
import androidx.compose.foundation.layout.Column as JColumn
import org.jetbrains.compose.common.ui.implementation

@Composable
actual fun Column(
    modifier: Modifier,
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable () -> Unit
) {
    JColumn(modifier = modifier.implementation, verticalArrangement = verticalArrangement.implementation) {
        content.invoke()
    }
}