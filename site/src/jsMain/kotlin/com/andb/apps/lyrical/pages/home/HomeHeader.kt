package com.andb.apps.lyrical.pages.home

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.components.widgets.phosphor.PhUser
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
import org.jetbrains.compose.web.css.px

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Logo()
        ProfilePicture()
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
private fun Logo() {
    Row(
        modifier = Modifier.gap(LyricalTheme.Spacing.LG),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image("LyricalIcon.svg", modifier = LogoStyle.toModifier())
        Heading2("Lyrical")
    }
}

@Composable
private fun ProfilePicture() {
    Box(
        modifier = Modifier.padding(LyricalTheme.Spacing.SM).background(LyricalTheme.palette.overlay).borderRadius(
            LyricalTheme.Radii.Circle),
        contentAlignment = Alignment.Center,
    ) {
        PhUser(modifier = Modifier.color(LyricalTheme.palette.contentSecondary).size(LyricalTheme.Size.Icon.Default))
    }
}