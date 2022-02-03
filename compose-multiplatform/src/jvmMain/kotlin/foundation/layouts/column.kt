package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.modifiers.ColumnScope
import compose.multiplatform.foundation.modifiers.toMultiplatformColumnScope
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Alignment
import androidx.compose.foundation.layout.Column as JColumn
import org.jetbrains.compose.common.ui.implementation

@Composable
actual fun Column(
    modifier: Modifier,
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable ColumnScope.() -> Unit
) {
    JColumn(
        modifier = modifier.implementation,
        verticalArrangement = verticalArrangement.implementation,
        horizontalAlignment = horizontalAlignment.implementation
    ) {
        content.invoke(this.toMultiplatformColumnScope())
    }
}