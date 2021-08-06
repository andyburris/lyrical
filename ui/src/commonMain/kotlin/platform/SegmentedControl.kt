package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.ui.Modifier


@Composable
fun <T> SegmentedControl(
    selected: T,
    options: List<T>,
    modifier: Modifier = Modifier,
    stringify: T.() -> String = { this.toString() },
    onSelect: (T) -> Unit,
) = ActualSegmentedControl(selected, options, modifier, stringify, onSelect)

@Composable
expect fun <T> ActualSegmentedControl(
    selected: T,
    options: List<T>,
    modifier: Modifier,
    stringify: T.() -> String,
    onSelect: (T) -> Unit,
)