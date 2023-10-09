package com.andb.apps.lyrical.pages.game.question

import SuggestionTrack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.adamratzman.spotify.models.SimpleAlbum
import com.adamratzman.spotify.models.Track
import com.andb.apps.lyrical.components.widgets.AlbumCover
import com.andb.apps.lyrical.components.widgets.Body
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.OutsetShadowStyle
import com.andb.apps.lyrical.theme.onInitialized
import com.andb.apps.lyrical.theme.onSubmit
import com.andb.apps.lyrical.util.artistsToString
import com.varabyte.kobweb.compose.css.BoxSizing
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.focus
import com.varabyte.kobweb.silk.components.style.hover
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.vh

const val MaxSuggestionAmount = 12

data class SuggestionSectionState(
    val searchTerm: String,
    val results: List<SuggestionTrack>,
    val selectedIndex: Int,
)

@Composable
fun SuggestionSection(
    suggestionSectionState: SuggestionSectionState,
    modifier: Modifier = Modifier,
    onSelect: (SuggestionTrack) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(LyricalTheme.Spacing.SM)
            .borderRadius(LyricalTheme.Radii.LG)
            .backgroundColor(LyricalTheme.palette.backgroundCard)
//            .maxHeight(33.vh)
            .overflow(Overflow.Hidden, Overflow.Scroll)
            .then(OutsetShadowStyle.toModifier())
    ) {
        suggestionSectionState.results.forEachIndexed { index, track ->
            SuggestionItem(
                track = track,
                isSelected = index == suggestionSectionState.selectedIndex,
                modifier = Modifier
                    .onSubmit { onSelect(track) }
            )
        }
        if (suggestionSectionState.results.isEmpty()) {
            Subtitle(
                text = "No results",
                color = LyricalTheme.palette.contentSecondary,
                modifier = Modifier
                    .padding(LyricalTheme.Spacing.SM)
            )
        }
    }
}

@Composable
private fun SuggestionItem(
    track: SuggestionTrack,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =  modifier
            .then(SuggestionItemStyle.toModifier())
            .padding(LyricalTheme.Spacing.MD)
            .borderRadius(LyricalTheme.Radii.MD)
            .gap(LyricalTheme.Spacing.LG)
            .then(if (isSelected) Modifier.backgroundColor(LyricalTheme.palette.divider) else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier
                .alignItems(AlignItems.Center)
                .gap(LyricalTheme.Spacing.MD)
        ) {
            AlbumCover(track.album, size = LyricalTheme.Size.Playlist.CoverSm)
            Subtitle(track.name)
        }
        Body(
            text = track.artists.joinToString(", "),
            color = LyricalTheme.palette.contentSecondary,
            modifier = Modifier.textAlign(TextAlign.End)
        )
    }
}

val SuggestionItemStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .cursor(Cursor.Pointer)
    }
    hover {
        Modifier.backgroundColor(LyricalTheme.paletteFrom(this.colorMode).overlay)
    }
}