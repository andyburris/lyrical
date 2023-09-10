package com.andb.apps.lyrical.pages.home.loggedIn.jumpbar

import androidx.compose.runtime.Composable
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.pages.home.loggedIn.HorizontalPlaylistItem
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.AlignItems

@Composable
fun SelectedPlaylists(
    selectedPlaylists: List<SimplePlaylist>,
    modifier: Modifier = Modifier,
    onRemovePlaylist: (SimplePlaylist) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(LyricalTheme.Spacing.LG)
    ) {
        selectedPlaylists.forEach { playlist ->
            Row(
                modifier = Modifier
                    .alignItems(AlignItems.Center)
            ) {
                HorizontalPlaylistItem(playlist)
            }
        }
    }
}