package com.andb.apps.lyrical.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
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
            .fontFamily("YoungSerif")
    }
    Breakpoint.SM {
        Modifier.fontSize(64.px)
    }
}
@Composable
fun Heading1(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    H1(modifier.then(Heading1Style.toModifier()).toAttrs()) {
        content()
    }
}

@Composable
fun Heading1(text: String, modifier: Modifier = Modifier) {
    H1(modifier.then(Heading1Style.toModifier()).toAttrs()) {
        Text(text)
    }
}


val Heading2Style by ComponentStyle {
    base {
        Modifier.fontSize(24.px)
            .fontFamily("YoungSerif")
    }
    Breakpoint.SM {
        Modifier.fontSize(48.px)
    }
}

@Composable
fun Heading2(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    H2(modifier.then(Heading2Style.toModifier()).toAttrs()) {
        content()
    }
}

@Composable
fun Heading2(text: String, modifier: Modifier = Modifier) {
    H2(modifier.then(Heading2Style.toModifier()).toAttrs()) {
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
        Modifier.fontSize(24.px)
    }
}

@Composable
fun Subtitle(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    P (modifier.then(SubtitleStyle.toModifier()).toAttrs()) {
        content()
    }
}

@Composable
fun Subtitle(text: String, modifier: Modifier = Modifier) {
    P (modifier.then(SubtitleStyle.toModifier()).toAttrs()) {
        Text(text)
    }
}


val BodyStyle by ComponentStyle {
    base {
        Modifier.fontSize(18.px)
            .fontWeight(FontWeight.SemiBold)
            .fontFamily("Inter")
    }
    Breakpoint.SM {
        Modifier.fontSize(24.px)
    }
}

@Composable
fun Body(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    P (modifier.then(BodyStyle.toModifier()).toAttrs()) {
        content()
    }
}

@Composable
fun Body(text: String, modifier: Modifier = Modifier) {
    P (modifier.then(BodyStyle.toModifier()).toAttrs()) {
        Text(text)
    }
}


val CaptionStyle by ComponentStyle {
    base {
        Modifier.fontSize(14.px)
            .fontWeight(FontWeight.SemiBold)
            .fontFamily("Inter")
    }
    Breakpoint.SM {
        Modifier.fontSize(18.px)
    }
}

@Composable
fun Caption(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    P (modifier.then(CaptionStyle.toModifier()).toAttrs()) {
        content()
    }
}
@Composable
fun Caption(text: String, modifier: Modifier = Modifier) {
    P (modifier.then(CaptionStyle.toModifier()).toAttrs()) {
        Text(text)
    }
}


