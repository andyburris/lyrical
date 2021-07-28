package room.lobby

import LobbyAction
import SpotifyRepository
import UserAction
import androidx.compose.runtime.Composable
import platform.Button
import platform.Text
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.ui.Modifier
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
    Column(modifier) { //TODO: turn into BottomSheetScaffold
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
            true -> ChoosePlaylists(state.playlists, spotifyRepository) {
                val newPlaylists = when {
                    it in state.playlists -> state.playlists - it
                    else -> state.playlists + it
                }
                onUserAction.invoke(LobbyAction.Host.UpdatePlaylists(newPlaylists))
            }
            false -> PlayerLobbyInfo(state.config)
        }
    }
}


@Composable
private fun ButtonRow(state: RoomState.Lobby, onStartGame: () -> Unit, onOpenOptions: () -> Unit) {
    Row {
        Button(
            isEnabled = state.playlists.isNotEmpty(),
            onClick = onStartGame
        ) {
            Text("Start Game".uppercase())
        }
        //TODO: add FAB
    }
}
