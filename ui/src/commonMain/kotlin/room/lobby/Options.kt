package room.lobby

import GameConfig
import androidx.compose.runtime.Composable
import common.Chip
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.ui.Modifier
import platform.CurrentPalette
import platform.LyricalTheme
import platform.SegmentedControl
import platform.Text

@Composable
fun EditableOptions(
    config: GameConfig,
    modifier: Modifier = Modifier,
    onEdit: (GameConfig) -> Unit
) {
    Column(modifier) { //TODO: Add Arrangement.spacedBy(24.dp)
        VerticalOptionItem("Difficulty") {
            SegmentedControl(
                selected = config.difficulty,
                options = Difficulty.values().toList(),
                stringify = { this.name },
                onSelect = { onEdit.invoke(config.copy(difficulty = it)) }
            )
        }
    }
}

@Composable
fun UneditableOptions(
    config: GameConfig,
    modifier: Modifier = Modifier
) {
    Column(modifier) { //TODO: Add Arrangement.spacedBy(24.dp)

    }
}

@Composable
private fun VerticalOptionItem(
    title: String,
    modifier: Modifier = Modifier,
    widget: @Composable () -> Unit,
) {
    Column(modifier) { //TODO: add Arrangement.spacedBy(4.dp)
        Text(title, style = LyricalTheme.typography.subtitle1, color = CurrentPalette.contentPrimary)
        widget()
    }
}

@Composable
private fun HorizontalOptionItem(
    title: String,
    modifier: Modifier = Modifier,
    widget: @Composable () -> Unit,
) {
    Row(modifier) {
        Text( //TODO: add Modifier.weight(1f)
            text = title,
            style = LyricalTheme.typography.subtitle1,
            color = CurrentPalette.contentPrimary
        )
        widget()
    }
}

@Composable
private fun <T> SegmentedOptions(selected: T, options: List<T>, stringify: T.() -> String = { this.toString() }) {
    Row {

    }
}