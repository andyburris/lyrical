package com.andb.apps.lyrical.components.layouts

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import kotlinx.browser.document
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.CSSAutoKeyword
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.*


val PageLayoutStyle by ComponentStyle {
    base { Modifier.width(100.vw).height(100.vh) }
}

val PageLayoutContainerStyle by ComponentStyle {
    base {
        Modifier
            .maxWidth(1200.px)
            .padding(24.px)
            .fillMaxWidth()
            .minHeight(100.percent)
            .styleModifier {
                property("margin", auto)
            }
    }

    Breakpoint.SM {
        Modifier.padding(48.px)
    }
}

@Composable
fun PageLayout(title: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    LaunchedEffect(title) {
        document.title = title
    }

    Column(modifier.then(PageLayoutStyle.toModifier())) {
        Column(
            modifier.then(PageLayoutContainerStyle.toModifier())
        ) {
            content()
        }
    }
}
