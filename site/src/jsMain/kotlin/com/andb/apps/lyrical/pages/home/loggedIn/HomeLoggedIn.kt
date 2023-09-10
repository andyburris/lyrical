package com.andb.apps.lyrical.pages.home.loggedIn

import GameConfig
import Screen
import SetupAction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.pages.home.loggedIn.jumpbar.JumpBar
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns

@Composable
fun HomeLoggedIn(state: Screen.Home.LoggedIn, modifier: Modifier = Modifier, onAction: (SetupAction) -> Unit) {
    val selectedPlaylists = remember { mutableStateOf(emptyList<SimplePlaylist>()) }
    val (userPlaylists, setUserPlaylists) = remember { mutableStateOf<List<SimplePlaylist>?>(null) }
    LaunchedEffect(Unit) {
        val up = state.spotifyRepository.getUserPlaylists()
        println("loaded user playlists, playlists = $up")
        setUserPlaylists(up)
    }

    Column(
        modifier = modifier
            .gap(LyricalTheme.Spacing.XXL)
            .fillMaxWidth(),
    ) {
        JumpBar(
            selectedPlaylists = selectedPlaylists.value,
            options = GameConfig(),
            modifier = Modifier.fillMaxWidth(),
            onStartGame = { onAction(SetupAction.StartGame(selectedPlaylists.value, GameConfig()) )},
        )
        SimpleGrid(
            numColumns = numColumns(2, 3, 4),
            modifier = Modifier
                .fillMaxWidth()
                .overflow(Overflow.Hidden)
                .gap(LyricalTheme.Spacing.XL),
        ) {
            when(userPlaylists) {
                null -> {}
                else -> userPlaylists.map { playlist ->
                    val isSelected = playlist in selectedPlaylists.value
                    VerticalPlaylistItem(
                        playlist = playlist,
                        isSelected = isSelected,
                        modifier = Modifier
                            .fillMaxWidth()
                            .cursor(Cursor.Pointer)
                            .onClick {
//                                val action = when {
//                                    playlist in selectedPlaylists.value -> SetupAction.RemovePlaylist(playlist)
//                                    else -> SetupAction.AddPlaylist(playlist)
//                                }
//                                onAction(action)
                                when {
                                    isSelected -> selectedPlaylists.value -= playlist
                                    else -> selectedPlaylists.value += playlist
                                }
                            }
                    )
                }
            }
        }
    }
}