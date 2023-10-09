package com.andb.apps.lyrical.pages.game.end

import GameAnswer
import GameHint
import GameQuestion
import androidx.compose.runtime.Composable
import com.adamratzman.spotify.models.SimpleAlbum
import com.andb.apps.lyrical.components.widgets.AlbumCover
import com.andb.apps.lyrical.components.widgets.Divider
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.components.widgets.phosphor.*
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.css.JustifyItems
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun SummaryItem(
    question: GameQuestion,
    questionIndex: Int,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onToggleExpand: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .gap(LyricalTheme.Spacing.LG)
            .then(when(isExpanded) {
                true -> Modifier
                    .padding(LyricalTheme.Spacing.LG)
                    .borderRadius(LyricalTheme.Radii.LG)
                    .border(1.px, LineStyle.Solid, LyricalTheme.Colors.accentPalette.divider)
                false -> Modifier
            })
    ) {
        SummaryItemTop(question, questionIndex, isExpanded, Modifier.onSubmit(block = onToggleExpand))
        if (isExpanded) {
            SummaryItemBottom(question)
        }
    }
}

@Composable
private fun SummaryItemTop(
    question: GameQuestion,
    questionIndex: Int,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val track = question.trackWithLyrics.sourcedTrack.track
    Row(
        modifier = modifier
            .gap(LyricalTheme.Spacing.MD)
            .alignItems(AlignItems.Center)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .flexGrow(1)
                .flexShrink(1)
                .gap(LyricalTheme.Spacing.LG)
                .alignItems(AlignItems.Center)
        ) {
            SummaryItemImage(questionIndex + 1, track.album, Modifier.flexShrink(0))
            Heading2(track.name, Modifier.flexShrink(1), color = LyricalTheme.Colors.accentPalette.contentPrimary)
        }
        Row(
            modifier = Modifier
                .gap(LyricalTheme.Spacing.SM)
                .flexShrink(0)
                .alignItems(AlignItems.Center)
        ) {
            Subtitle(
                text = when(val answer = question.answer) {
                    is GameAnswer.Answered.Correct -> "+${answer.points} pts"
                    is GameAnswer.Answered.Incorrect -> "Incorrect"
                    is GameAnswer.Answered.Skipped -> "Skipped"
                    is GameAnswer.Unanswered -> throw Error("Should be no unanswered questions on EndScreen")
                },
                color = when(question.answer) {
                    is GameAnswer.Answered.Correct -> LyricalTheme.Colors.accentPalette.contentPrimary
                    is GameAnswer.Answered.Incorrect, is GameAnswer.Answered.Skipped -> LyricalTheme.Colors.accentPalette.contentSecondary
                    is GameAnswer.Unanswered -> throw Error("Should be no unanswered questions on EndScreen")
                },
                modifier = Modifier.flexShrink(0)
            )
            when(isExpanded) {
                true -> PhCaretUp(Modifier.color(LyricalTheme.Colors.accentPalette.contentSecondary))
                false -> PhCaretDown(Modifier.color(LyricalTheme.Colors.accentPalette.contentSecondary))
            }
        }
    }
}

@Composable
private fun SummaryItemImage(
    questionNumber: Int,
    album: SimpleAlbum,
    modifier: Modifier = Modifier,
) {
    Box(modifier.placeItems(com.varabyte.kobweb.compose.css.AlignItems.Center, JustifyItems.Center)) {
        AlbumCover(
            album = album,
            size = LyricalTheme.Size.Playlist.CoverSummary,
            modifier = Modifier.opacity(.5)
        )
        Subtitle(
            text = questionNumber.toString(),
            color = LyricalTheme.Colors.accentPalette.contentPrimary,
            modifier = Modifier.zIndex(1)
        )
    }
}

@Composable
private fun SummaryItemBottom(
    question: GameQuestion,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .gap(LyricalTheme.Spacing.LG)
            .fillMaxWidth()
            .color(LyricalTheme.Colors.accentPalette.contentPrimary),
    ) {
        AnswerInfoItem(
            icon = { PhQuotes() },
            text = { Subtitle {
                Span(Modifier.color(LyricalTheme.Colors.accentPalette.contentSecondary).toAttrs()) { Text("“") }
                Span { Text(question.lyric) }
                if (GameHint.NextLine in question.answer.hintsUsed) {
                    Span(Modifier.color(LyricalTheme.Colors.accentPalette.contentSecondary).toAttrs()) { Text(" / ") }
                    Span { Text(question.nextLyric) }
                }
                Span(Modifier.color(LyricalTheme.Colors.accentPalette.contentSecondary).toAttrs()) { Text("”") }
            } },
        )
        AnswerInfoItem(
            icon = { PhUserCircle() },
            text = question.artist,
        )
        AnswerInfoItem(
            icon = { PhPlaylist() },
            text = { Subtitle {
                Span(Modifier.color(LyricalTheme.Colors.accentPalette.contentSecondary).toAttrs()) { Text("from ") }
                Span { Text(question.playlist.name) }
            } },
        )

        Divider(color = LyricalTheme.Colors.accentPalette.divider)

        when(val answer = question.answer) {
            is GameAnswer.Answered.Incorrect -> AnswerInfoItem(
                icon = { PhEraser() },
                text = { Subtitle {
                    Span(Modifier.color(LyricalTheme.Colors.accentPalette.contentSecondary).toAttrs()) { Text("you said “") }
                    Span { Text(answer.answer) }
                    Span(Modifier.color(LyricalTheme.Colors.accentPalette.contentSecondary).toAttrs()) { Text("”") }
                } },
            )
            else -> {}
        }
        AnswerInfoItem(
            icon = { PhCircleWavyQuestion() },
            text = { Subtitle {
                Span(Modifier.color(LyricalTheme.Colors.accentPalette.contentSecondary).toAttrs()) { Text("used hints: ") }
                Span {
                    val text = when {
                        question.answer.hintsUsed.isEmpty() -> "None"
                        else -> question.answer.hintsUsed.joinToString(", ") {
                            when(it) {
                                GameHint.Artist -> "Artist"
                                GameHint.NextLine -> "Next Line"
                            }
                        }
                    }
                    Text(text)
                }
            } },
        )

    }
}

@Composable
fun AnswerInfoItem(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .gap(LyricalTheme.Spacing.MD)
            .alignItems(AlignItems.Center),
    ) {
        icon()
        text()
    }
}

@Composable
fun AnswerInfoItem(
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    AnswerInfoItem(icon, { Subtitle(text) }, modifier)
}