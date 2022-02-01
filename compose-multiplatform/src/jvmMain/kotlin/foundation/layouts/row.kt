package compose.multiplatform.foundation.layout

import compose.multiplatform.ui.Modifier
import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.modifiers.RowScope
import compose.multiplatform.foundation.modifiers.toMultiplatformRowScope
import compose.multiplatform.ui.Arrangement
import androidx.compose.foundation.layout.Row as JRow
import org.jetbrains.compose.common.ui.implementation
import compose.multiplatform.ui.Alignment

@Composable
actual fun Row(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable RowScope.() -> Unit
) {
    JRow(
        modifier.implementation,
        horizontalArrangement.implementation,
        verticalAlignment.implementation
    ) {
        content.invoke(this.toMultiplatformRowScope())
    }
}