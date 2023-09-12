package com.andb.apps.lyrical.pages.home.loggedIn.jumpbar

import GameOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.OutsetShadowStyle
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.*

enum class ExpandedJumpBarTab { None, SelectedPlaylists, Options }
@Composable
fun JumpBar(
    selectedPlaylists: List<SimplePlaylist>,
    options: GameOptions,
    modifier: Modifier = Modifier,
    onStartGame: () -> Unit,
) {
    val expandedTab = remember { mutableStateOf(ExpandedJumpBarTab.None) }

    Column(
        modifier = modifier
            .then(OutsetShadowStyle.toModifier())
            .borderRadius(LyricalTheme.Radii.MD)
            .overflow(Overflow.Hidden)

    ) {
        JumpBarTabs(
            expandedTab = expandedTab.value,
            selectedPlaylists = selectedPlaylists,
            onExpandTab = { expandedTab.value = it },
            onStartGame = onStartGame
        )
        when(expandedTab.value) {
            ExpandedJumpBarTab.None -> {}
            ExpandedJumpBarTab.SelectedPlaylists -> SelectedPlaylists(selectedPlaylists) {}
            ExpandedJumpBarTab.Options -> OptionsEditor(options) {}
        }
    }
}