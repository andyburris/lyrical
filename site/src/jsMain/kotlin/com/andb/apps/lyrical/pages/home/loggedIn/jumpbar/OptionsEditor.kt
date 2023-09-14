package com.andb.apps.lyrical.pages.home.loggedIn.jumpbar

import Difficulty
import GameOptions
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.SegmentedControl
import com.andb.apps.lyrical.components.widgets.SegmentedControlItem
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.components.widgets.Switch
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
        OptionItem("Show source playlist", layoutInline = true) {
            Switch(
                checked = currentOptions.showSourcePlaylist,
                onCheckedChange = { onUpdateOptions(currentOptions.copy(showSourcePlaylist = it))},
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
