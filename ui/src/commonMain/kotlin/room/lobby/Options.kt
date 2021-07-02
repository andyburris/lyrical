package room.lobby

import GameConfig
import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp

@Composable
fun EditableOptions(
    config: GameConfig,
    modifier: Modifier = Modifier,
    onEdit: (GameConfig) -> Unit
) {
    Column(modifier) { //TODO: Add Arrangement.spacedBy(24.dp)

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