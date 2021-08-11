package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.ui.Modifier

@Composable
actual fun <T> ActualSegmentedControl(
    selected: T,
    options: List<T>,
    modifier: Modifier,
    stringify: T.() -> String,
    onSelect: (T) -> Unit,
) = Box(modifier) {} //TODO: actually implement