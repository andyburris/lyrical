package room.lobby

import LobbyAction
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
fun LobbyScreen(code: RoomCode, isHost: Boolean, state: RoomState.Lobby, modifier: Modifier = Modifier, onUserAction: (UserAction) -> Unit) {
    Column(modifier) { //TODO: turn into BottomSheetScaffold
        LobbyHeader(code, isHost, state.playlists, onClickPlaylist = { onUserAction.invoke(LobbyAction.Host.UpdatePlaylists(state.playlists - it)) })
        if (isHost) {
            ButtonRow(
                state = state,
                onStartGame = { onUserAction.invoke(UserAction.Host.LoadGame) },
                onOpenOptions = {} //TODO: open BottomSheet in BottomSheetScaffold
            )
        }
        when(isHost) {
            true -> ChoosePlaylists(state.playlists)
            false -> PlayerLobbyInfo(state.config)
        }
    }
}


@Composable
private fun ButtonRow(state: RoomState.Lobby, onStartGame: () -> Unit, onOpenOptions: () -> Unit) {
    Row {
        Button(
            isEnabled = state.playlists.isNotEmpty(),
            onClick = { }
        ) {
            Text("Start Game".uppercase())
        }
        //TODO: add FAB
    }
}
