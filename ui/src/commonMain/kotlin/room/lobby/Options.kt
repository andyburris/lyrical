package room.lobby

import GameConfig
import androidx.compose.runtime.Composable
import common.Chip
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.foundation.layout.fillMaxWidth
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp
import platform.*

@OptIn(ExperimentalComposeWebWidgetsApi::class)
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

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
fun UneditableOptions(
    config: GameConfig,
    modifier: Modifier = Modifier
) {
    Column(modifier) { //TODO: Add Arrangement.spacedBy(24.dp)

    }
}

@OptIn(ExperimentalComposeWebWidgetsApi::class)
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

@OptIn(ExperimentalComposeWebWidgetsApi::class)
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
@OptIn(ExperimentalComposeWebWidgetsApi::class)
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
