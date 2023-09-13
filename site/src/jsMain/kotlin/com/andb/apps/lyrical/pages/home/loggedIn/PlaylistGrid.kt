package com.andb.apps.lyrical.pages.home.loggedIn

import androidx.compose.runtime.Composable
import com.adamratzman.spotify.models.FeaturedPlaylists
import com.adamratzman.spotify.models.SimplePlaylist
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns

@Composable
fun PlaylistGrid(
    playlists: List<SimplePlaylist>?,
    selectedPlaylists: List<SimplePlaylist>,
    modifier: Modifier = Modifier,
    onToggle: (SimplePlaylist, isSelected: Boolean) -> Unit,
) {
    SimpleGrid(
        numColumns = numColumns(2, 3, 4),
        modifier = modifier
            .fillMaxWidth()
            .overflow(Overflow.Hidden)
            .gap(LyricalTheme.Spacing.XL),
    ) {
        when(playlists) {
            null -> {}
            else -> playlists.map { playlist ->
                val isSelected = playlist in selectedPlaylists
                VerticalPlaylistItem(
                    playlist = playlist,
                    isSelected = isSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .cursor(Cursor.Pointer)
                        .onSubmit {
                            onToggle(playlist, isSelected)
                        }
                )
            }
        }
    }
}