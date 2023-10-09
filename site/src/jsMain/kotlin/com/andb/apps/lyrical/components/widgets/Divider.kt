package com.andb.apps.lyrical.components.widgets

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Hr

@Composable
fun Divider(
    modifier: Modifier = Modifier,
    color: Color = LyricalTheme.palette.divider,
) {
    Div(modifier
        .backgroundColor(color)
        .then(DividerStyle.toModifier()).toAttrs())
}

val DividerStyle by ComponentStyle.base {
    Modifier
        .fillMaxWidth()
        .height(1.px)
}