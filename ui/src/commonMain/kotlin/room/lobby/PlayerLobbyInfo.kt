package room.lobby

import GameConfig
import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import platform.LyricalTheme

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