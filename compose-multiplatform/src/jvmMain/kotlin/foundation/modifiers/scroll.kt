package compose.multiplatform.foundation.modifier

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.internal.castOrCreate
import compose.multiplatform.ui.Modifier

@Composable
actual fun Modifier.verticalScroll(): Modifier = castOrCreate().apply {
    modifier = modifier.verticalScroll(rememberScrollState())
}

@Composable
actual fun Modifier.horizontalScroll(): Modifier = castOrCreate().apply {
    modifier = modifier.horizontalScroll(rememberScrollState())
}