package com.andb.apps.lyrical.pages.game.answer

import androidx.compose.runtime.Composable
import com.adamratzman.spotify.models.Track
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.theme.LyricalPalette
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.util.artistsToString
import com.andb.apps.lyrical.util.imageOrPlaceholder
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.AlignItems

@Composable
fun AnswerItem(
    track: Track,
    palette: LyricalPalette,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .alignItems(AlignItems.Center)
            .gap(LyricalTheme.Spacing.LG)
    ) {
        Image(
            src = track.album.imageOrPlaceholder(),
            desc = "Cover art of ${track.name} from the album ${track.album} by ${track.artists.artistsToString()}",
            modifier = Modifier.size(LyricalTheme.Size.Playlist.CoverHorizontal),
        )
        Column {
            Heading2(track.name, color = palette.contentPrimary)
            Heading2(track.artists.artistsToString(), color = palette.contentSecondary)
        }
    }
}