package com.andb.apps.lyrical.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2


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