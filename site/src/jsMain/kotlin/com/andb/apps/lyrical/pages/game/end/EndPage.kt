package com.andb.apps.lyrical.pages.game.end

import GameQuestion
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.components.sections.GameAppBar
import com.andb.apps.lyrical.components.widgets.Heading1
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.theme.LyricalPalette
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun EndPage(
    endScreen: Screen.GameScreen.End,
    modifier: Modifier = Modifier,
) {
    val router = rememberPageContext().router
    PageLayout("Lyrical - Summary", modifier.backgroundColor(LyricalTheme.Colors.accentPalette.background)) {
        Column(
            modifier = Modifier
                .gap(LyricalTheme.Spacing.XXL)
                .fillMaxSize()
        ) {
            GameAppBar(
                gameScreen = endScreen,
                modifier = Modifier.fillMaxWidth(),
                palette = LyricalTheme.Colors.accentPalette,
                onClose = { router.navigateTo("/") }
            )
            Column(
                modifier = Modifier.gap(LyricalTheme.Spacing.XXL),
            ) {
                Column {
                    Subtitle("Your Score", color = LyricalTheme.Colors.accentPalette.contentSecondary)
                    Heading1 {
                        Span(Modifier.color(LyricalTheme.Colors.accentPalette.contentPrimary).toAttrs()) { Text("${endScreen.game.points}/${endScreen.game.questions.size}") }
                        Span(Modifier.color(LyricalTheme.Colors.accentPalette.contentSecondary).toAttrs()) { Text(" pts") }
                    }
                }
                Column(Modifier.gap(LyricalTheme.Spacing.LG)) {
                    val expandedItem = remember { mutableStateOf<GameQuestion?>(null) }
                    Subtitle("Your Answers", color = LyricalTheme.Colors.accentPalette.contentSecondary)
                    endScreen.game.questions.forEachIndexed { index, question ->
                        SummaryItem(
                            question = question,
                            questionIndex = index,
                            isExpanded = expandedItem.value == question,
                            onToggleExpand = { when(expandedItem.value) {
                                question -> expandedItem.value = null
                                else -> expandedItem.value = question
                            } }
                        )
                    }
                }
            }

        }
    }
}