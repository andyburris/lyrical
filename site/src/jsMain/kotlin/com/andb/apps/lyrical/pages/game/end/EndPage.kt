package com.andb.apps.lyrical.pages.game.end

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.Heading1
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.theme.LyricalPalette
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun EndPage(
    endScreen: Screen.GameScreen.End,
    palette: LyricalPalette,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Column {
            Subtitle("Your Score", color = palette.contentSecondary)
            Heading1 {
                Span(Modifier.color(palette.contentPrimary).toAttrs()) { Text("${endScreen.game.points}/${endScreen.game.questions.size}") }
                Span(Modifier.color(palette.contentSecondary).toAttrs()) { Text(" pts") }
            }
        }
    }
}