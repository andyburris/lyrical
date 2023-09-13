package com.andb.apps.lyrical.pages.home.loggedIn.jumpbar

import Difficulty
import GameOptions
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.SegmentedControl
import com.andb.apps.lyrical.components.widgets.SegmentedControlItem
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.OutsetShadowStyle
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorScheme
import com.varabyte.kobweb.silk.theme.colors.ColorSchemes
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent

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
                colorScheme = object : ColorScheme {
                    override val _50 = LyricalTheme.Colors.accentPalette.background
                    override val _100 = LyricalTheme.Colors.accentPalette.background
                    override val _200 = LyricalTheme.Colors.accentPalette.background
                    override val _300 = LyricalTheme.Colors.accentPalette.background
                    override val _400 = LyricalTheme.Colors.accentPalette.background
                    override val _500 = LyricalTheme.Colors.accentPalette.background
                    override val _600 = LyricalTheme.Colors.accentPalette.background
                    override val _700 = LyricalTheme.Colors.accentPalette.background
                    override val _800 = LyricalTheme.Colors.accentPalette.background
                    override val _900 = LyricalTheme.Colors.accentPalette.background
                },
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
        true -> Row(fullModifier) {
            Subtitle(title, Modifier.flexGrow(1))
            widget()
        }
        false -> Column(fullModifier) {
            Subtitle(title)
            widget()
        }
    }
}
