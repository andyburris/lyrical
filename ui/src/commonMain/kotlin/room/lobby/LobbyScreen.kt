package room.lobby

import LobbyAction
import SpotifyRepository
import UserAction
import androidx.compose.runtime.Composable
import common.Icon
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.foundation.modifier.verticalScroll
import compose.multiplatform.foundation.modifiers.height
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp
import platform.Button
import server.RoomCode
import server.RoomState

@Composable
fun LobbyScreen(
    code: RoomCode,
    isHost: Boolean,
    state: RoomState.Lobby,
    spotifyRepository: SpotifyRepository,
    modifier: Modifier = Modifier,
    onUserAction: (UserAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(modifier.verticalScroll()) { //TODO: turn into BottomSheetScaffold
        LobbyHeader(
            code = code,
            isHost = isHost,
            playlists = state.playlists,
            onClickPlaylist = { onUserAction.invoke(LobbyAction.Host.UpdatePlaylists(state.playlists - it)) },
            onNavigateBack = onNavigateBack
        )
        if (isHost) {
            ButtonRow(
                state = state,
                onStartGame = { onUserAction.invoke(UserAction.Host.LoadGame) },
                onOpenOptions = {} //TODO: open BottomSheet in BottomSheetScaffold
            )
        }
        when (isHost) {
            true -> Column(
                modifier = Modifier.padding(32.dp).padding(top = 32.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                EditableOptions(state.config) {
                    onUserAction.invoke(LobbyAction.Host.UpdateConfig(it))
                }
                ChoosePlaylists(state.playlists, spotifyRepository) {
                    val newPlaylists = when {
                        it in state.playlists -> state.playlists - it
                        else -> state.playlists + it
                    }
                    onUserAction.invoke(LobbyAction.Host.UpdatePlaylists(newPlaylists))
                }
            }
            false -> PlayerLobbyInfo(state.config)
        }
    }
}


@Composable
private fun ButtonRow(state: RoomState.Lobby, modifier: Modifier = Modifier, onStartGame: () -> Unit, onOpenOptions: () -> Unit) {
    Row(
        modifier.height(0.dp).padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            modifier = Modifier.height(56.dp),
            isEnabled = state.playlists.isNotEmpty(),
            onClick = onStartGame
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Icon(Icon.Play, contentDescription = null)
                Text("Start Game".uppercase())
            }
        }
        //FloatingActionButton()
        //TODO: add FAB
    }
}
