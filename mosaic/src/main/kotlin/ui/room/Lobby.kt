package ui.room

import GameConfig
import User
import UserAction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.TextStyle
import common.Actions
import common.AppBar
import common.Divider
import data.Key
import data.TerminalAction
import data.plus
import model.GenericPlaylist
import model.name
import name
import server.RoomCode
import server.RoomState
import server.isValid

@Composable
fun Lobby(roomCode: RoomCode, user: User, host: User, state: RoomState.Lobby, onUserAction: (UserAction) -> Unit) {

    when (user) {
        host -> HostContent(roomCode, state, user, onUserAction)
        else -> PlayerContent(roomCode, state, user, host) { onUserAction.invoke(UserAction.LeaveRoom) }
    }
}

@Composable
private fun PlayerContent(roomCode: RoomCode, state: RoomState.Lobby, user: User, host: User, onLeaveRoom: () -> Unit) {
    AppBar("Lobby", roomCode)
    Divider()
    Users(state.joinedUsers, host = host, user = user)
    Playlists(state.playlists)
    Options(state.config)
    Actions(
        TerminalAction("Leave Room", Key.CtrlLeft + Key.L) {
            onLeaveRoom.invoke()
        }
    )
}

private enum class HostEditing {
    RemoveUser, RemovePlaylist, AddPlaylist, Options
}

@Composable
private fun HostContent(roomCode: RoomCode, state: RoomState.Lobby, user: User, onUserAction: (UserAction) -> Unit) {
    val editing = remember { mutableStateOf<HostEditing?>(null) }
    when(editing.value) {
        HostEditing.RemoveUser -> TODO()
        HostEditing.RemovePlaylist -> TODO()
        HostEditing.AddPlaylist -> AddPlaylist()
        HostEditing.Options -> TODO()
        null -> HostContentHome(roomCode, state, user, onUserAction) { editing.value = it }
    }
}

@Composable
private fun HostContentHome(roomCode: RoomCode, state: RoomState.Lobby, user: User, onUserAction: (UserAction) -> Unit, onUserEdit: (HostEditing) -> Unit) {
    AppBar("Lobby", roomCode)
    Divider()
    Users(state.joinedUsers, host = user, user = user)
    Playlists(state.playlists)
    Options(state.config)
    val startActionIfValid = when(state.isValid) {
        true -> listOf(
            TerminalAction("Start Game", Key.CtrlLeft + Key.S) {
                onUserAction.invoke(UserAction.Host.LoadGame)
            }
        )
        false -> emptyList()
    }
    val otherActions = listOf(
        TerminalAction("Add Playlist", Key.CtrlLeft + Key.P) {
            onUserEdit.invoke(HostEditing.AddPlaylist)
        },
        TerminalAction("Remove Playlist", Key.CtrlLeft + Key.R) {
            onUserEdit.invoke(HostEditing.RemovePlaylist)
        },
        TerminalAction("Kick User", Key.CtrlLeft + Key.K) {
            onUserEdit.invoke(HostEditing.RemoveUser)
        },
        TerminalAction("Change Options", Key.CtrlLeft + Key.O) {
            onUserEdit.invoke(HostEditing.Options)
        },
        TerminalAction("Leave Room", Key.CtrlLeft + Key.L) {
            onUserAction.invoke(UserAction.LeaveRoom)
        }
    )
    Actions(*(startActionIfValid + otherActions).toTypedArray())
}

@Composable
private fun AddPlaylist() {
    AppBar("Lobby", "Adding Playlist")
    Divider()
}

@Composable
private fun RemovePlaylist() {
    AppBar("Lobby", "Removing Playlist")
    Divider()
}

@Composable
fun Users(users: List<User>, user: User, host: User) {
    Text("Users", style = TextStyle.Dim)
    val usersText = users.joinToString(" • ") { it.name + when  {
        it == user && it == host -> " (Me, Host)"
        it == user -> " (Me)"
        it == host -> " (Host)"
        else -> ""
    } }
    Text(usersText)
}

@Composable
fun Playlists(playlists: List<GenericPlaylist>) {
    Text("Playlists", style = TextStyle.Dim)
    val playlistsText = playlists.joinToString(" • ") { it.name }
    Text(playlistsText)
}

@Composable
fun Options(config: GameConfig) {
    Text("Options", style = TextStyle.Dim)
    Text("Timer: ${config.timer}")
    Text("Amount of songs: ${config.amountOfSongs}")
    Text("Difficulty: ${config.difficulty}")
    Text("Show source playlist: ${config.showSourcePlaylist}")
    Text("Distribute playlists evenly: ${config.distributePlaylistsEvenly}")
}