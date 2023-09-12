package com.andb.apps.lyrical.components.sections

import Game
import Screen
import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.components.widgets.phosphor.PhX
import com.andb.apps.lyrical.theme.LyricalPalette
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.dom.Caption
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun GameAppBar(
    gameScreen: Screen.GameScreen,
    palette: LyricalPalette = LyricalTheme.palette,
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
) {

    Row(
        modifier = modifier
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.SpaceBetween)
    ) {
        Row(
            modifier = Modifier
                .alignItems(AlignItems.Center)
                .gap(LyricalTheme.Spacing.MD)
        ) {
            PhX(
                modifier = Modifier
                    .color(palette.contentSecondary)
                    .onClick { onClose() } //TODO: add touch target
            )
            when(gameScreen) {
                is Screen.GameScreen.Loading -> Subtitle("Loading", color = palette.contentSecondary)
                is Screen.GameScreen.Question -> Column {
                    Subtitle("Question ${gameScreen.game.currentQuestionIndex + 1}/${gameScreen.game.questions.size}")
                    if (gameScreen.game.options.showSourcePlaylist) {
                        Caption {
                            Span(Modifier.color(palette.contentTertiary).toAttrs()) { Text("from") }
                            Span(Modifier.color(palette.contentSecondary).toAttrs()) { Text(gameScreen.game.currentQuestion?.playlist?.name ?: "") }
                        }
                    }
                }
                is Screen.GameScreen.Answer -> Subtitle(
                    text = "Question ${gameScreen.game.lastQuestionIndex + 1}/${gameScreen.game.questions.size}",
                    color = palette.contentSecondary
                )
                is Screen.GameScreen.End -> Subtitle("Summary", color = palette.contentSecondary)
            }
        }

        when(gameScreen) {
            is Screen.GameScreen.Loading, is Screen.GameScreen.End -> {}
            is Screen.GameScreen.Question -> PointsText(gameScreen.game, palette)
            is Screen.GameScreen.Answer -> PointsText(gameScreen.game, palette)
        }
    }
}

@Composable
private fun PointsText(
    game: Game,
    palette: LyricalPalette,
    modifier: Modifier = Modifier,
) {
    Subtitle(modifier = modifier) {
        Span(Modifier.color(palette.contentSecondary).toAttrs()) { Text("${game.points}") }
        Span(Modifier.color(palette.contentTertiary).toAttrs()) { Text("/${game.lastQuestionIndex + 1} pts") }
    }
}