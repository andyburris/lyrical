package com.andb.apps.lyrical.pages.game.question

import SpotifyRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.andb.apps.lyrical.components.widgets.Button
import com.andb.apps.lyrical.components.widgets.Heading2Style
import com.andb.apps.lyrical.components.widgets.phosphor.PhSkipForward
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.OutsetShadowStyle
import com.andb.apps.lyrical.theme.onInitialized
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.Input
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.unaryMinus
import org.jetbrains.compose.web.css.vw

@Composable
fun AnswerSection(
    modifier: Modifier = Modifier,
    onSkip: () -> Unit,
    onAnswer: (String) -> Unit,
) {
    val searchTerm = remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .padding(LyricalTheme.Spacing.XL)
            .borderRadius(topLeft = LyricalTheme.Radii.XL, topRight = LyricalTheme.Radii.XL)
            .gap(LyricalTheme.Spacing.LG)
            .backgroundColor(LyricalTheme.palette.backgroundCard)
            .then(OutsetShadowStyle.toModifier())
    ) {
        Input(
            type = InputType.Text,
            value = searchTerm.value,
            onValueChanged = { searchTerm.value = it },
            onCommit = {
                if (searchTerm.value.isNotBlank()) {
                    onAnswer(searchTerm.value)
                }
            },
            placeholder = "Song name",
            modifier = Modifier
                .then(Heading2Style.toModifier())
                .border(0.px)
                .height(Height.FitContent)
                .padding(0.px)
                .fillMaxWidth()
                .onInitialized { ref -> ref.focus() }
        )
        Row(
            modifier = Modifier.gap(LyricalTheme.Spacing.SM)
        ) {
            Button(
                text = "Answer",
                modifier = Modifier.onClick {
                    if (searchTerm.value.isNotBlank()) {
                        onAnswer(searchTerm.value)
                    }
                },
                isEnabled = searchTerm.value.isNotBlank(),
                palette = LyricalTheme.Colors.accentPalette,
            )
            Button(
                text = null,
                icon = { PhSkipForward() },
                modifier = Modifier.onClick { onSkip() }
            )
        }
    }
}

val AnswerSectionStyle by ComponentStyle {
    base {
        Modifier
            .margin(-(24.px))
            .maxWidth(1200.px)
            .width(100.vw)
    }
    Breakpoint.SM {
        Modifier.margin(-(48.px))
    }
}