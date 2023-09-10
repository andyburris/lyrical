package com.andb.apps.lyrical.pages.home.loggedIn.jumpbar

import androidx.compose.runtime.Composable
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.components.widgets.Caption
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.components.widgets.phosphor.*
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.css.Cursor
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

@Composable
fun JumpBarTabs(
    expandedTab: ExpandedJumpBarTab,
    selectedPlaylists: List<SimplePlaylist>,
    modifier: Modifier = Modifier,
    onExpandTab: (ExpandedJumpBarTab) -> Unit,
    onStartGame: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        SelectedTab(
            selectedPlaylists = selectedPlaylists,
            expandedTab = expandedTab,
            modifier = Modifier
                .then(if (expandedTab != ExpandedJumpBarTab.Options) Modifier.flexGrow(1) else Modifier)
                .onClick {
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
                .onClick {
                    onExpandTab(when(expandedTab) {
                        ExpandedJumpBarTab.Options -> ExpandedJumpBarTab.None
                        else -> ExpandedJumpBarTab.Options
                    })
                }
        )
        StartGameTab(
            modifier = Modifier
                .onClick { onStartGame() }
        )
    }
}

val JumpBarTabStyle by ComponentStyle {
    base { Modifier.cursor(Cursor.Pointer) }
}

val DefaultJumpBarTabVariant by JumpBarTabStyle.addVariantBase {
    Modifier.backgroundColor(LyricalTheme.paletteFrom(colorMode).backgroundCard)
}
val StartGameJumpBarTabVariant by JumpBarTabStyle.addVariantBase {
    Modifier
        .backgroundColor(LyricalTheme.Colors.accentPalette.background)
        .color(LyricalTheme.Colors.accentPalette.contentPrimary)
}
@Composable
private fun StartGameTab(modifier: Modifier = Modifier) {
    ResponsiveTabContent(
        icon = { PhPlayCircle() },
        text = { Subtitle("Start Game") },
        expandedInMobile = false,
        modifier = modifier
            .padding(LyricalTheme.Spacing.MD)
            .then(JumpBarTabStyle.toModifier(StartGameJumpBarTabVariant))
    )
}


@Composable
private fun SelectedTab(
    selectedPlaylists: List<SimplePlaylist>,
    expandedTab: ExpandedJumpBarTab,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .then(JumpBarTabStyle.toModifier(DefaultJumpBarTabVariant))
            .gap(LyricalTheme.Spacing.MD)
            .padding(LyricalTheme.Spacing.MD)
            .justifyContent(JustifyContent.SpaceBetween)
            .alignItems(AlignItems.Center)
    ) {
        ResponsiveTabContent(
            icon = { SelectedPlaylistPreview(selectedPlaylists) },
            text = { Subtitle("${selectedPlaylists.size} Selected") },
            expandedInMobile = expandedTab != ExpandedJumpBarTab.Options,
        )

        when(expandedTab) {
            ExpandedJumpBarTab.None -> PhCaretDown(Modifier.color(LyricalTheme.palette.contentSecondary))
            ExpandedJumpBarTab.SelectedPlaylists -> PhCaretUp(Modifier.color(LyricalTheme.palette.contentSecondary))
            ExpandedJumpBarTab.Options -> {}
        }
    }
}

@Composable
private fun SelectedPlaylistPreview(selectedPlaylists: List<SimplePlaylist>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .margin(left = 12.px),
    ) {
        val hasOverflow = selectedPlaylists.size > 4
        selectedPlaylists.take(if (hasOverflow) 3 else 4).forEach { playlist ->
            Image(
                src = playlist.images.firstOrNull()?.url ?: "",
                desc = "Cover of ${playlist.name}",
                modifier = Modifier
                    .margin(left = -(12.px))
                    .border(2.px, LineStyle.Solid, LyricalTheme.palette.backgroundCard)
                    .borderRadius(8.px)
                    .size(LyricalTheme.Size.Playlist.CoverSm),
            )
        }
        if (hasOverflow) {
            Row(
                modifier = Modifier
                    .margin(left = -(12.px))
                    .alignItems(AlignItems.Center)
                    .justifyContent(JustifyContent.Center)
                    .backdropFilter(blur(2.px))
                    .backgroundColor(LyricalTheme.palette.overlayDark)
                    .border(2.px, LineStyle.Solid, LyricalTheme.palette.backgroundCard)
                    .borderRadius(8.px)
                    .size(LyricalTheme.Size.Playlist.CoverSm)
            ) {
                Caption("+" + (selectedPlaylists.size - 3), color = Color.rgb(255, 255, 255))
            }
        }
        if (selectedPlaylists.isEmpty()) {
            Row(
                modifier = Modifier
                    .margin(left = -(12.px))
                    .alignItems(AlignItems.Center)
                    .justifyContent(JustifyContent.Center)
                    .backgroundColor(LyricalTheme.palette.overlay)
                    .border(2.px, LineStyle.Solid, LyricalTheme.palette.backgroundCard)
                    .borderRadius(8.px)
                    .size(LyricalTheme.Size.Playlist.CoverSm)
            ) {
                PhMusicNotesPlus(size = LyricalTheme.Spacing.MD, modifier = Modifier.color(LyricalTheme.palette.contentSecondary))
            }
        }
    }
}

@Composable
private fun OptionsTab(expandedTab: ExpandedJumpBarTab, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .then(JumpBarTabStyle.toModifier(DefaultJumpBarTabVariant))
            .gap(LyricalTheme.Spacing.MD)
            .padding(LyricalTheme.Spacing.MD)
            .justifyContent(JustifyContent.SpaceBetween)
            .alignItems(AlignItems.Center)
    ) {
        ResponsiveTabContent(
            icon = { PhFadersHorizontal() },
            text = { Subtitle("Options") },
            expandedInMobile = expandedTab == ExpandedJumpBarTab.Options,
        )
        if (expandedTab == ExpandedJumpBarTab.Options) PhCaretUp(Modifier.color(LyricalTheme.palette.contentSecondary))
    }
}

@Composable
private fun ResponsiveTabContent(icon: @Composable () -> Unit, text: @Composable () -> Unit, expandedInMobile: Boolean, modifier: Modifier = Modifier) {
    val breakpoint = rememberBreakpoint()
    when(breakpoint) {
        Breakpoint.ZERO -> Row(modifier = modifier.gap(LyricalTheme.Spacing.SM).alignItems(AlignItems.Center)) {
            icon()
            if (expandedInMobile) text()
        }
        else -> Column(
            modifier = modifier.gap(LyricalTheme.Spacing.XS),
        ) {
            icon()
            text()
        }
    }
}