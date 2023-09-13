package com.andb.apps.lyrical.pages.home.loggedIn

import androidx.compose.runtime.Composable
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.components.widgets.Body
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.components.widgets.phosphor.PhCheck
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.px

@Composable
fun VerticalPlaylistItem(playlist: SimplePlaylist, isSelected: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .gap(LyricalTheme.Spacing.MD)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .minWidth(LyricalTheme.Size.Playlist.CoverLg)
                .aspectRatio(1)
                .borderRadius(LyricalTheme.Radii.SM)
                .overflow(Overflow.Hidden),
        ) {
            Image(
                src = playlist.images.firstOrNull()?.url ?: "",
                desc = "Cover of ${playlist.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            if (isSelected) {
                Row(
                    modifier = Modifier
                        .backgroundColor(LyricalTheme.palette.overlayDark)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .alignItems(AlignItems.Center)
                        .justifyContent(JustifyContent.Center)
                ) {
                    PhCheck(Modifier.color(Color.rgb(255, 255, 255)), size = 64.px)
                }
            }
        }
        Column {
            Subtitle(playlist.name)
            Body(playlist.owner.displayName ?: "", color = LyricalTheme.palette.contentSecondary)
        }
    }
}

@Composable
fun HorizontalPlaylistItem(
    playlist: SimplePlaylist,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .alignItems(AlignItems.Center)
            .gap(LyricalTheme.Spacing.MD)
    ) {
        Image(
            src = playlist.images.firstOrNull()?.url ?: "",
            desc = "Cover of ${playlist.name}",
            modifier = Modifier
                .size(LyricalTheme.Size.Playlist.CoverMd)
                .borderRadius(LyricalTheme.Radii.XS)
        )
        Column(Modifier.fillMaxWidth()) {
            Subtitle(playlist.name)
            Body(playlist.owner.displayName ?: "", color = LyricalTheme.palette.contentSecondary)
        }
    }
}