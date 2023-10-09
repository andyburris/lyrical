package com.andb.apps.lyrical.pages.home.loggedIn.jumpbar

import Difficulty
import GameOptions
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.*
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.AlignItems

@Composable
fun OptionsEditor(
    currentOptions: GameOptions,
    modifier: Modifier = Modifier,
    onUpdateOptions: (GameOptions) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(LyricalTheme.Spacing.MD)
            .gap(LyricalTheme.Spacing.MD),
    ) {
        OptionItem("Difficulty",) {
            SegmentedControl(
                options = Difficulty.entries.toList(),
                item = {
                    SegmentedControlItem(
                        item = it,
                        isSelected = it == currentOptions.difficulty,
                        modifier = Modifier.onSubmit { onUpdateOptions(currentOptions.copy(difficulty =  it)) },
                        content = { difficulty ->
                            Subtitle(difficulty.name)
                        }
                    )
                },
            )
        }
        OptionItem("Number of questions", layoutInline = false) {
            Row(
                modifier = Modifier
                    .gap(LyricalTheme.Spacing.LG)
                    .fillMaxWidth()
                    .alignItems(AlignItems.Center)
            ) {
                Slider(
                    currentValue = currentOptions.amountOfSongs,
                    possibleRange = 1..20,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { onUpdateOptions(currentOptions.copy(amountOfSongs = it)) }
                )
                Subtitle(currentOptions.amountOfSongs.toString())
            }
        }
        OptionItem("Show source playlist", layoutInline = true) {
            Switch(
                checked = currentOptions.showSourcePlaylist,
                onCheckedChange = { onUpdateOptions(currentOptions.copy(showSourcePlaylist = it))},
            )
        }
        OptionItem("Distribute playlists evenly", layoutInline = true) {
            Switch(
                checked = currentOptions.distributePlaylistsEvenly,
                onCheckedChange = { onUpdateOptions(currentOptions.copy(distributePlaylistsEvenly = it))},
            )
        }
        OptionItem("Show suggestions", layoutInline = true) {
            Switch(
                checked = currentOptions.showSuggestions,
                onCheckedChange = { onUpdateOptions(currentOptions.copy(showSuggestions = it))},
            )
        }
    }
}

@Composable
private fun OptionItem(
    title: String,
    modifier: Modifier = Modifier,
    layoutInline: Boolean = false,
    widget: @Composable () -> Unit,
) {
    val fullModifier = modifier
        .gap(LyricalTheme.Spacing.SM)
        .fillMaxWidth()
    when(layoutInline) {
        true -> Row(
            modifier = fullModifier
                .alignItems(AlignItems.Center)
        ) {
            Subtitle(title, Modifier.flexGrow(1))
            widget()
        }
        false -> Column(fullModifier) {
            Subtitle(title)
            widget()
        }
    }
}
