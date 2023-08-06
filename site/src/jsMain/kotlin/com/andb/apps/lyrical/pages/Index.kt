package com.andb.apps.lyrical.pages

import androidx.compose.runtime.*
import com.andb.apps.lyrical.LyricalTheme
import com.varabyte.kobweb.core.Page
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.components.widgets.Heading2
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.SilkTheme
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text

@Page
@Composable
fun HomePage() {
    PageLayout("Lyrical - Home") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Header()
            ProfilePicture()
        }
    }
}

val LogoStyle by ComponentStyle {
    base {
        Modifier.size(32.px)
    }
    Breakpoint.SM {
        Modifier.size(56.px)
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.gap(LyricalTheme.Spacing.LG),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image("LyricalIcon.svg", modifier = LogoStyle.toModifier())
        Heading2 { Text("Lyrical") }
    }
}

@Composable
private fun ProfilePicture() {
    Box(Modifier.size(LyricalTheme.Size.Button.SM).background(SilkTheme.palette.overlay).borderRadius(LyricalTheme.Radii.Circle))
}