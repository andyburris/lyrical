package com.andb.apps.lyrical.pages.home.loggedIn.jumpbar

import androidx.compose.runtime.Composable
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.components.widgets.phosphor.PhX
import com.andb.apps.lyrical.pages.home.loggedIn.HorizontalPlaylistItem
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.AlignItems

@Composable
fun SelectedPlaylists(
    selectedPlaylists: List<SimplePlaylist>,
    modifier: Modifier = Modifier,
    onRemovePlaylist: (SimplePlaylist) -> Unit,
) {
    Column(
        modifier = modifier
            .gap(LyricalTheme.Spacing.SM)
            .padding(top = LyricalTheme.Spacing.SM, bottom = LyricalTheme.Spacing.MD, leftRight = LyricalTheme.Spacing.MD)
            .fillMaxWidth()
    ) {
        selectedPlaylists.forEach { playlist ->
            Row(
                modifier = Modifier
                    .alignItems(AlignItems.Center)
                    .gap(LyricalTheme.Spacing.SM)
                    .fillMaxWidth()
            ) {
                HorizontalPlaylistItem(playlist, modifier = Modifier.flexGrow(1))
                PhX(
                    modifier = Modifier
                        .color(LyricalTheme.palette.contentSecondary)
                        .onSubmit { onRemovePlaylist(playlist) }
                )
            }
        }
    }
}