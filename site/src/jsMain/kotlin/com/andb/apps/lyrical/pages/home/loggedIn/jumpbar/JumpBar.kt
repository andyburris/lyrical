package com.andb.apps.lyrical.pages.home.loggedIn.jumpbar

import GameOptions
import SetupState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.OutsetShadowStyle
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

enum class ExpandedJumpBarTab { None, SelectedPlaylists, Options }

@Composable
fun JumpBarHorizontal(
    setupState: SetupState,
    modifier: Modifier = Modifier,
    onRemovePlaylist: (SimplePlaylist) -> Unit,
    onUpdateOptions: (GameOptions) -> Unit,
    onStartGame: () -> Unit,
) {
    val (expandedTab, onExpandTab) = remember { mutableStateOf(ExpandedJumpBarTab.None) }

    Column(
        modifier = modifier
            .backgroundColor(LyricalTheme.palette.backgroundCard)
            .then(OutsetShadowStyle.toModifier())
            .borderRadius(LyricalTheme.Radii.MD)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SelectedTab(
                selectedPlaylists = setupState.selectedPlaylists,
                expandedTab = expandedTab,
                modifier = Modifier
                    .borderRadius(topLeft = LyricalTheme.Radii.MD, bottomLeft = if (expandedTab == ExpandedJumpBarTab.None) LyricalTheme.Radii.MD else 0.px)
                    .then(if (expandedTab != ExpandedJumpBarTab.Options) Modifier.flexGrow(1) else Modifier)
                    .onSubmit {
                        onExpandTab(when(expandedTab) {
                            ExpandedJumpBarTab.SelectedPlaylists -> ExpandedJumpBarTab.None
                            else -> ExpandedJumpBarTab.SelectedPlaylists
                        })
                    }
            )
            OptionsTab(
                expandedTab = expandedTab,
                modifier = Modifier
                    .borderLeft(1.px, LineStyle.Solid, LyricalTheme.palette.divider)
                    .then(if (expandedTab == ExpandedJumpBarTab.Options) Modifier.flexGrow(1) else Modifier)
                    .onSubmit {
                        onExpandTab(when(expandedTab) {
                            ExpandedJumpBarTab.Options -> ExpandedJumpBarTab.None
                            else -> ExpandedJumpBarTab.Options
                        })
                    }
            )
            StartGameTab(
                modifier = Modifier
                    .borderRadius(topRight = LyricalTheme.Radii.MD, bottomRight = if (expandedTab == ExpandedJumpBarTab.None) LyricalTheme.Radii.MD else 0.px)
                    .opacity(if (setupState.selectedPlaylists.isEmpty()) 0.3 else 1)
                    .onSubmit(isEnabled = setupState.selectedPlaylists.isNotEmpty()) { onStartGame() }
            )
        }

        when(expandedTab) {
            ExpandedJumpBarTab.None -> {}
            ExpandedJumpBarTab.SelectedPlaylists -> SelectedPlaylists(setupState.selectedPlaylists, onRemovePlaylist = onRemovePlaylist)
            ExpandedJumpBarTab.Options -> OptionsEditor(setupState.options, onUpdateOptions = onUpdateOptions)
        }
    }
}

@Composable
fun JumpBarVertical(
    setupState: SetupState,
    modifier: Modifier = Modifier,
    onRemovePlaylist: (SimplePlaylist) -> Unit,
    onUpdateOptions: (GameOptions) -> Unit,
    onStartGame: () -> Unit,
) {
    val (expandedTab, onExpandTab) = remember { mutableStateOf(ExpandedJumpBarTab.None) }

    Column(
        modifier = modifier
            .gap(LyricalTheme.Spacing.MD),
    ) {
        StartGameTab(
            modifier = Modifier
                .fillMaxWidth()
                .then(OutsetShadowStyle.toModifier())
                .borderRadius(LyricalTheme.Radii.MD)
                .overflow(Overflow.Hidden)
                .opacity(if (setupState.selectedPlaylists.isEmpty()) 0.3 else 1)
                .cursor(if (setupState.selectedPlaylists.isEmpty()) Cursor.Default else Cursor.Pointer)
                .onSubmit {
                    if (setupState.selectedPlaylists.isNotEmpty()) {
                        onStartGame()
                    }
                }
        )

        Column(
            modifier = Modifier
                .backgroundColor(LyricalTheme.palette.backgroundCard)
                .fillMaxWidth()
                .then(OutsetShadowStyle.toModifier())
                .borderRadius(LyricalTheme.Radii.MD)
                .overflow(Overflow.Hidden)
        ) {
            SelectedTab(
                selectedPlaylists = setupState.selectedPlaylists,
                expandedTab = expandedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .onSubmit {
                        onExpandTab(when(expandedTab) {
                            ExpandedJumpBarTab.SelectedPlaylists -> ExpandedJumpBarTab.None
                            else -> ExpandedJumpBarTab.SelectedPlaylists
                        })
                    }
            )
            if (expandedTab == ExpandedJumpBarTab.SelectedPlaylists) {
                SelectedPlaylists(setupState.selectedPlaylists, onRemovePlaylist = onRemovePlaylist)
            }
            OptionsTab(
                expandedTab = expandedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .borderTop(1.px, LineStyle.Solid, LyricalTheme.palette.divider)
                    .then(if (expandedTab == ExpandedJumpBarTab.Options) Modifier.flexGrow(1) else Modifier)
                    .onSubmit {
                        onExpandTab(when(expandedTab) {
                            ExpandedJumpBarTab.Options -> ExpandedJumpBarTab.None
                            else -> ExpandedJumpBarTab.Options
                        })
                    }
            )
            if (expandedTab == ExpandedJumpBarTab.SelectedPlaylists) {
                OptionsEditor(setupState.options, onUpdateOptions = onUpdateOptions)
            }
        }
    }
}