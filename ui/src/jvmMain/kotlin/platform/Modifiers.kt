package platform

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.internal.castOrCreate
import org.jetbrains.compose.common.ui.Modifier

actual fun Modifier.weight(weight: Float): Modifier = castOrCreate().apply {
    //modifier = modifier.weight(weight)
}

@Composable
actual fun Modifier.verticalScroll(): Modifier = castOrCreate().apply {
    modifier = modifier.verticalScroll(rememberScrollState())
}