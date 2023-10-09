package com.andb.apps.lyrical.pages.home

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.Button
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.components.widgets.phosphor.PhMoonStars
import com.andb.apps.lyrical.components.widgets.phosphor.PhSun
import com.andb.apps.lyrical.components.widgets.phosphor.PhUser
import com.andb.apps.lyrical.theme.onSubmit
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
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Col

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Logo()
        Row(
            modifier = Modifier
                .gap(LyricalTheme.Spacing.MD),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val colorModeState = ColorMode.currentState
            Button(
                text = null,
                modifier = Modifier.onClick { colorModeState.value = when(colorModeState.value) {
                    ColorMode.LIGHT -> ColorMode.DARK
                    ColorMode.DARK -> ColorMode.LIGHT
                } },
                icon = {
                    when(ColorMode.current) {
                        ColorMode.LIGHT -> PhMoonStars(Modifier.color(LyricalTheme.palette.contentSecondary))
                        ColorMode.DARK -> PhSun(Modifier.color(LyricalTheme.palette.contentSecondary))
                    }
                }
            )
            Button(
                text = null,
                modifier = Modifier,
                icon = {
                    PhUser(Modifier.color(LyricalTheme.palette.contentSecondary))
                }
            )
//            ProfilePicture()
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