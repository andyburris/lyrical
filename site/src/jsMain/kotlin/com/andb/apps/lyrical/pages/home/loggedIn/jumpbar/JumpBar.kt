package com.andb.apps.lyrical.pages.home.loggedIn.jumpbar

import GameConfig
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.components.widgets.Caption
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.components.widgets.phosphor.*
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.functions.blur
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariantBase
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.*

enum class ExpandedJumpBarTab { None, SelectedPlaylists, Options }
@Composable
fun JumpBar(
    selectedPlaylists: List<SimplePlaylist>,
    options: GameConfig,
    modifier: Modifier = Modifier,
    onStartGame: () -> Unit,
) {
    val expandedTab = remember { mutableStateOf(ExpandedJumpBarTab.None) }

    Column(
        modifier = modifier
            .border(1.px, LineStyle.Solid, LyricalTheme.palette.divider)
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