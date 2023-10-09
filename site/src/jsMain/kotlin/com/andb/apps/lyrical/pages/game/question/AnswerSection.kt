package com.andb.apps.lyrical.pages.game.question

import SuggestionTrack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.adamratzman.spotify.models.Track
import com.andb.apps.lyrical.components.widgets.*
import com.andb.apps.lyrical.components.widgets.phosphor.PhSkipForward
import com.andb.apps.lyrical.components.widgets.phosphor.PhX
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.OutsetShadowStyle
import com.andb.apps.lyrical.theme.onInitialized
import com.andb.apps.lyrical.theme.onSubmit
import com.andb.apps.lyrical.util.artistsToString
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.forms.Input
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vw

private sealed class SearchTerm {
    data class Text(val term: String) : SearchTerm()
    data class Suggestion(val track: SuggestionTrack) : SearchTerm()
}

@Composable
fun AnswerSectionNoSuggestions(
    modifier: Modifier = Modifier,
    onSkip: () -> Unit,
    onAnswer: (String) -> Unit,
) {
    val (searchTerm, setSearchTerm) = remember { mutableStateOf("") }
    AnswerSectionCard(modifier) {
        Input(
            type = InputType.Text,
            value = searchTerm,
            onValueChanged = { setSearchTerm(it) },
            onCommit = {
                if (searchTerm.isNotBlank()) {
                    onAnswer(searchTerm)
                }
            },
            placeholder = "Song name",
            modifier = Modifier
                .then(Heading1Style.toModifier())
                .border(0.px)
                .height(Height.FitContent)
                .padding(0.px)
                .fillMaxWidth()
                .onInitialized { ref -> ref.focus() }
        )
        AnswerSectionButtons(
            isAnswerEnabled = searchTerm.isNotBlank(),
            onAnswer = { onAnswer(searchTerm) },
            onSkip = { onSkip() },
        )
    }
}

@Composable
fun AnswerSectionWithSuggestions(
    allSuggestions: List<SuggestionTrack>,
    modifier: Modifier = Modifier,
    onSkip: () -> Unit,
    onAnswer: (SuggestionTrack) -> Unit,
) {
    val (searchTerm, setSearchTerm) = remember { mutableStateOf<SearchTerm>(SearchTerm.Text("")) }
    val suggestionState = remember(searchTerm) {
        if (searchTerm !is SearchTerm.Text || searchTerm.term.isBlank()) return@remember null
        val results = allSuggestions
            .filter { track -> searchTerm.term.lowercase() in track.name.lowercase() || track.artists.any { searchTerm.term.lowercase() in it.lowercase() } }
            .take(MaxSuggestionAmount)
        mutableStateOf(SuggestionSectionState(searchTerm.term, results, 0))
    }
    Column(
        modifier = modifier
            .gap(LyricalTheme.Spacing.MD)
    ) {
        if (suggestionState != null) {
            Row(Modifier.padding(leftRight = LyricalTheme.Spacing.SM).fillMaxWidth()) {
                SuggestionSection(suggestionState.value) {
                    setSearchTerm(SearchTerm.Suggestion(it))
                }
            }
        }
        AnswerSectionCard {
            when(searchTerm) {
                is SearchTerm.Text -> Input(
                    type = InputType.Text,
                    value = searchTerm.term,
                    onValueChanged = { setSearchTerm(SearchTerm.Text(it)) },
                    onCommit = {
                        if (suggestionState != null) {
                            val selectedTrack = suggestionState.value.results[suggestionState.value.selectedIndex]
                            setSearchTerm(SearchTerm.Suggestion(selectedTrack))
                        }
                    },
                    placeholder = "Song name",
                    modifier = Modifier
                        .then(Heading1Style.toModifier())
                        .border(0.px)
                        .height(Height.FitContent)
                        .padding(0.px)
                        .fillMaxWidth()
                        .onInitialized { ref -> ref.focus() }
                        .onKeyDown {
                            println("keydown = ${it.key}")
                            when(it.key) {
                                "ArrowUp" -> {
                                    it.preventDefault()
                                    suggestionState!!.value = suggestionState.value.copy(
                                        selectedIndex = (suggestionState.value.selectedIndex - 1)
                                            .coerceIn(0 until suggestionState.value.results.size)
                                    )
                                }
                                "ArrowDown" -> {
                                    it.preventDefault()
                                    suggestionState!!.value = suggestionState.value.copy(
                                        selectedIndex = (suggestionState.value.selectedIndex + 1)
                                            .coerceIn(0 until suggestionState.value.results.size)
                                    )
                                }
                            }
                        }
                )
                is SearchTerm.Suggestion -> Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gap(LyricalTheme.Spacing.SM),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        modifier = Modifier.gap(LyricalTheme.Spacing.SM),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AlbumCover(searchTerm.track.album, LyricalTheme.Size.Playlist.CoverHorizontal)
                        Column(Modifier.flexGrow(1)) {
                            Subtitle(searchTerm.track.name)
                            Body(searchTerm.track.artists.joinToString(", "), color = LyricalTheme.palette.contentSecondary)
                        }
                    }
                    PhX(
                        modifier = Modifier
                            .color(LyricalTheme.palette.contentSecondary)
                            .cursor(Cursor.Pointer)
                            .onSubmit { setSearchTerm(SearchTerm.Text("")) }
                    )
                }
            }
            AnswerSectionButtons(
                isAnswerEnabled = searchTerm is SearchTerm.Suggestion,
                onAnswer = { onAnswer((searchTerm as SearchTerm.Suggestion).track) },
                onSkip = { onSkip() },
            )
        }
    }
}

@Composable
private fun AnswerSectionCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(LyricalTheme.Spacing.XL)
            .borderRadius(topLeft = LyricalTheme.Radii.XL, topRight = LyricalTheme.Radii.XL)
            .gap(LyricalTheme.Spacing.LG)
            .backgroundColor(LyricalTheme.palette.backgroundCard)
            .then(OutsetShadowStyle.toModifier())
    ) {
        content()
    }
}

@Composable
private fun AnswerSectionButtons(
    isAnswerEnabled: Boolean,
    modifier: Modifier = Modifier,
    onAnswer: () -> Unit,
    onSkip: () -> Unit,
) {
    Row(
        modifier = modifier.gap(LyricalTheme.Spacing.SM)
    ) {
        Button(
            text = "Answer",
            modifier = Modifier.onSubmit(isEnabled = isAnswerEnabled, isButton = true) {
                onAnswer()
            },
            isEnabled = isAnswerEnabled,
            palette = LyricalTheme.Colors.accentPalette,
        )
        Button(
            text = null,
            icon = { PhSkipForward() },
            modifier = Modifier.onSubmit(isButton = true) { onSkip() }
        )
    }
}

val AnswerSectionStyle by ComponentStyle {
    base {
        Modifier
            .maxWidth(1200.px)
            .width(100.vw)
            .styleModifier {
                property("margin-left", auto)
                property("margin-right", auto)
            }
    }
}