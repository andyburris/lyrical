package room.lobby

import GameConfig
import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.ui.Modifier
import platform.*

@Composable
fun EditableOptions(
    config: GameConfig,
    modifier: Modifier = Modifier,
    onEdit: (GameConfig) -> Unit
) {
    Column(modifier) { //TODO: Add Arrangement.spacedBy(24.dp)
        VerticalOptionItem("Difficulty") {
/*            SegmentedControl(
                selected = config.difficulty,
                options = Difficulty.values().toList(),
                stringify = { this.name },
                onSelect = { onEdit.invoke(config.copy(difficulty = it)) },
                modifier = Modifier.fillMaxWidth()
            )*/
        }
        HorizontalOptionItem("Show source playlist") {
            Switch(
                checked = config.showSourcePlaylist,
                onCheckedChange = { onEdit.invoke(config.copy(showSourcePlaylist = it)) },
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

/*
@Composable
private fun <T> SegmentedControl(
    selected: T,
    options: List<T>,
    modifier: Modifier = Modifier,
    stringify: T.() -> String = { this.toString() },
    onSelect: (T) -> Unit,
) {
    Row(modifier) {
        options.forEach { option ->
            val isSelected = option == selected
            Text(
                text = option.stringify(),
                modifier = if (isSelected) Modifier.background(CurrentPalette.invert().background).padding(8.dp) else Modifier, //TODO: simplify when Modifier.then() is added, add Modifier.weight()
                color = if (isSelected) CurrentPalette.invert().contentPrimary else CurrentPalette.contentSecondary
            )
        }
    }
}*/
