package client

import model.GenericPlaylist
import server.RoomCode

sealed class Screen {
    object Home : Screen()
    object Join : Screen()
    data class Room(val roomCode: RoomCode) : Screen()
    object Test : Screen()
}


sealed class PlaylistSearchState {
    data class Results(val playlists: List<SelectedPlaylist>) : PlaylistSearchState()
    object RequiresLogin : PlaylistSearchState()
    object Loading : PlaylistSearchState()
    object Error : PlaylistSearchState()
}

data class SelectedPlaylist(val playlist: GenericPlaylist, val selected: Boolean)