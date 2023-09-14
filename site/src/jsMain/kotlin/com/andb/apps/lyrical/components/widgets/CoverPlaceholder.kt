package com.andb.apps.lyrical.components.widgets

import androidx.compose.runtime.Composable
import com.adamratzman.spotify.models.SimpleAlbum
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.components.widgets.phosphor.PhDisc
import com.andb.apps.lyrical.components.widgets.phosphor.PhPlaylist
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.util.artistsToString
import com.varabyte.kobweb.compose.css.calc
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.*

@Composable
fun PlaylistCover(
    playlist: SimplePlaylist,
    size: CSSNumeric,
    iconSize: CSSNumeric = calc { size / 3 },
    cornerSize: CSSNumeric = calc { size / 4 },
    modifier: Modifier = Modifier,
    roundCorners: Boolean = true,
) {
    when(val playlistArt = playlist.images.firstOrNull()?.url) {
        null -> PlaylistCoverPlaceholder(size, roundCorners, iconSize, cornerSize, modifier)
        else -> Image(
            src = playlistArt,
            desc = "Cover of ${playlist.name}" + (playlist.owner.displayName?.let { "by $it" } ?: ""),
            modifier = modifier
                .size(size)
                .then(if(roundCorners) Modifier.borderRadius(cornerSize) else Modifier)
        )
    }
}

@Composable
fun PlaylistCoverPlaceholder(
    size: CSSNumeric,
    roundCorners: Boolean,
    iconSize: CSSNumeric,
    cornerSize: CSSNumeric,
    modifier: Modifier = Modifier,
) {
    CoverPlaceholder(size, cornerSize, roundCorners, modifier) { modifier ->
        PhPlaylist(modifier, iconSize)
    }
}

@Composable
fun AlbumCover(
    album: SimpleAlbum,
    size: CSSNumeric,
    iconSize: CSSNumeric = calc { size / 2 },
    cornerSize: CSSNumeric = calc { size / 4 },
    modifier: Modifier = Modifier,
    roundCorners: Boolean = true,
) {
    when(val albumArt = album.images.firstOrNull()?.url) {
        null -> AlbumCoverPlaceholder(size, roundCorners, iconSize, cornerSize, modifier)
        else -> Image(
            src = albumArt,
            desc = "Cover of ${album.name} by ${album.artists.artistsToString()}",
            modifier = modifier
                .size(size)
                .then(if(roundCorners) Modifier.borderRadius(calc { size / 8 }) else Modifier)
        )
    }
}

@Composable
fun AlbumCoverPlaceholder(
    size: CSSNumeric,
    roundCorners: Boolean,
    iconSize: CSSNumeric,
    cornerSize: CSSNumeric,
    modifier: Modifier = Modifier,
) {
    CoverPlaceholder(size, cornerSize, roundCorners, modifier) { modifier ->
        PhDisc(modifier, iconSize)
    }
}


@Composable
private fun CoverPlaceholder(
    size: CSSNumeric,
    cornerSize: CSSNumeric,
    roundCorners: Boolean,
    modifier: Modifier = Modifier,
    icon: @Composable (modifier: Modifier) -> Unit,
) {
    return Row(
        modifier = modifier
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.Center)
            .size(size)
            .backgroundColor(LyricalTheme.palette.backgroundDark)
            .then(if(roundCorners) Modifier.borderRadius(cornerSize) else Modifier)
    ) {
        icon(
            Modifier
                .color(LyricalTheme.palette.contentSecondary),
        )
    }
}