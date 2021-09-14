package room.lobby

import GameConfig
import androidx.compose.runtime.Composable
import platform.LyricalTheme
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
fun PlayerLobbyInfo(config: GameConfig, modifier: Modifier = Modifier) {
    Column(modifier) {

        UneditableOptions(
            config = config,
            modifier = Modifier
                //.clip(LyricalTheme.shapes.medium) TODO: readd once shapes are supported
                .background(LyricalTheme.colors.backgroundDark)
                .padding(24.dp)
        )
    }
}