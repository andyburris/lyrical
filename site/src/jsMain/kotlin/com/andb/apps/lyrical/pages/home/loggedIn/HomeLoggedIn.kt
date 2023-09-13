package com.andb.apps.lyrical.pages.home.loggedIn

import Screen
import SetupState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.pages.home.loggedIn.jumpbar.JumpBarHorizontal
import com.andb.apps.lyrical.pages.home.loggedIn.jumpbar.JumpBarVertical
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.px

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

    when(rememberBreakpoint()) {
        Breakpoint.ZERO, Breakpoint.SM, Breakpoint.MD -> Column(
            modifier = modifier
                .gap(LyricalTheme.Spacing.XXL)
                .fillMaxWidth(),
        ) {
            JumpBarHorizontal(
                setupState = screen.setupState,
                modifier = Modifier.fillMaxWidth(),
                onRemovePlaylist = { onUpdateSetupState(screen.setupState.copy(selectedPlaylists = screen.setupState.selectedPlaylists - it)) },
                onUpdateOptions = { onUpdateSetupState(screen.setupState.copy(options = it))},
                onStartGame = { onStartGame(screen.setupState) },
            )
            PlaylistGrid(userPlaylists, screen.setupState.selectedPlaylists) { playlist, isSelected ->
                onUpdateSetupState(screen.setupState.copy(selectedPlaylists = when {
                    isSelected -> screen.setupState.selectedPlaylists - playlist
                    else -> screen.setupState.selectedPlaylists + playlist
                }))
            }
        }
        else -> Row(
            modifier = modifier
                .gap(LyricalTheme.Spacing.XXL)
                .fillMaxWidth(),
        ) {
            JumpBarVertical(
                setupState = screen.setupState,
                modifier = Modifier
                    .width(333.px)
                    .flexShrink(0),
                onRemovePlaylist = { onUpdateSetupState(screen.setupState.copy(selectedPlaylists = screen.setupState.selectedPlaylists - it)) },
                onUpdateOptions = { onUpdateSetupState(screen.setupState.copy(options = it))},
                onStartGame = { onStartGame(screen.setupState) },
            )
            PlaylistGrid(userPlaylists, screen.setupState.selectedPlaylists) { playlist, isSelected ->
                onUpdateSetupState(screen.setupState.copy(selectedPlaylists = when {
                    isSelected -> screen.setupState.selectedPlaylists - playlist
                    else -> screen.setupState.selectedPlaylists + playlist
                }))
            }
        }
    }
}