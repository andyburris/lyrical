package com.andb.apps.lyrical.components.widgets

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


val Heading1Style by ComponentStyle {
    base {
        Modifier.fontSize(42.px)
            .fontFamily("Young Serif")
    }
    Breakpoint.SM {
        Modifier.fontSize(64.px)
    }
}
@Composable
fun Heading1(modifier: Modifier = Modifier, color: Color? = null, content: @Composable () -> Unit) {
    H1(modifier
        .then(Heading1Style.toModifier())
        .then(if (color != null) Modifier.color(color) else Modifier)
        .toAttrs()) {
        content()
    }
}

@Composable
fun Heading1(text: String, modifier: Modifier = Modifier, color: Color? = null) {
    H1(modifier
        .then(Heading1Style.toModifier())
        .then(if (color != null) Modifier.color(color) else Modifier)
        .toAttrs()) {
        Text(text)
    }
}


val Heading2Style by ComponentStyle {
    base {
        Modifier.fontSize(24.px)
            .fontFamily("Young Serif")
    }
    Breakpoint.SM {
        Modifier.fontSize(48.px)
    }
}

@Composable
fun Heading2(modifier: Modifier = Modifier, color: Color? = null, content: @Composable () -> Unit) {
    H2(modifier
        .then(Heading2Style.toModifier())
        .then(if (color != null) Modifier.color(color) else Modifier)
        .toAttrs()) {
        content()
    }
}

@Composable
fun Heading2(text: String, modifier: Modifier = Modifier, color: Color? = null) {
    H2(modifier
        .then(Heading2Style.toModifier())
        .then(if (color != null) Modifier.color(color) else Modifier)
        .toAttrs()) {
        Text(text)
    }
}


val SubtitleStyle by ComponentStyle {
    base {
        Modifier.fontSize(18.px)
            .fontWeight(FontWeight.SemiBold)
            .fontFamily("Inter")
    }
    Breakpoint.SM {
        Modifier.fontSize(22.px)
    }
}

@Composable
fun Subtitle(modifier: Modifier = Modifier, color: Color? = null, content: @Composable () -> Unit) {
    P (modifier
        .then(SubtitleStyle.toModifier())
        .then(if (color != null) Modifier.color(color) else Modifier)
        .toAttrs()) {
        content()
    }
}

@Composable
fun Subtitle(text: String, modifier: Modifier = Modifier, color: Color? = null) {
    P (modifier
        .then(SubtitleStyle.toModifier())
        .then(if (color != null) Modifier.color(color) else Modifier)
        .toAttrs()) {
        Text(text)
    }
}


val BodyStyle by ComponentStyle {
    base {
        Modifier.fontSize(18.px)
            .fontWeight(FontWeight.Normal)
            .fontFamily("Inter")
    }
    Breakpoint.SM {
        Modifier.fontSize(22.px)
    }
}

@Composable
fun Body(modifier: Modifier = Modifier, color: Color? = null, content: @Composable () -> Unit) {
    P (modifier
        .then(BodyStyle.toModifier())
        .then(if (color != null) Modifier.color(color) else Modifier)
        .toAttrs()) {
        content()
    }
}

@Composable
fun Body(text: String, modifier: Modifier = Modifier, color: Color? = null) {
    P (modifier
        .then(BodyStyle.toModifier())
        .then(if (color != null) Modifier.color(color) else Modifier)
        .toAttrs()) {
        Text(text)
    }
}


val CaptionStyle by ComponentStyle {
    base {
        Modifier.fontSize(14.px)
            .fontWeight(FontWeight.Normal)
            .fontFamily("Inter")
    }
    Breakpoint.SM {
        Modifier.fontSize(18.px)
    }
}

@Composable
fun Caption(modifier: Modifier = Modifier, content: @Composable () -> Unit, color: Color? = null) {
    P (
        modifier
            .then(CaptionStyle.toModifier())
            .then(if (color != null) Modifier.color(color) else Modifier)
            .toAttrs()) {
        content()
    }
}
@Composable
fun Caption(text: String, modifier: Modifier = Modifier, color: Color? = null) {
    P (
        modifier
            .then(CaptionStyle.toModifier())
            .then(if (color != null) Modifier.color(color) else Modifier)
            .toAttrs()) {
        Text(text)
    }
}


