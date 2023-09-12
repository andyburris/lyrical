package com.andb.apps.lyrical.pages.home.loggedIn

import GameOptions
import Screen
import SetupState
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
fun HomeLoggedIn(
    screen: Screen.Home.LoggedIn,
    modifier: Modifier = Modifier,
    onUpdateSetupState: (SetupState) -> Unit,
    onStartGame: (SetupState) -> Unit,
) {
    val (userPlaylists, setUserPlaylists) = remember { mutableStateOf<List<SimplePlaylist>?>(null) }
    LaunchedEffect(Unit) {
        setUserPlaylists(screen.spotifyRepository.getUserPlaylists())
    }

    Column(
        modifier = modifier
            .gap(LyricalTheme.Spacing.XXL)
            .fillMaxWidth(),
    ) {
        JumpBar(
            selectedPlaylists = screen.setupState.selectedPlaylists,
            options = GameOptions(),
            modifier = Modifier.fillMaxWidth(),
            onStartGame = { onStartGame(screen.setupState) },
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
                    val isSelected = playlist in screen.setupState.selectedPlaylists
                    VerticalPlaylistItem(
                        playlist = playlist,
                        isSelected = isSelected,
                        modifier = Modifier
                            .fillMaxWidth()
                            .cursor(Cursor.Pointer)
                            .onClick {
                                onUpdateSetupState(screen.setupState.copy(selectedPlaylists = when {
                                    isSelected -> screen.setupState.selectedPlaylists - playlist
                                    else -> screen.setupState.selectedPlaylists + playlist
                                }))
                            }
                    )
                }
            }
        }
    }
}