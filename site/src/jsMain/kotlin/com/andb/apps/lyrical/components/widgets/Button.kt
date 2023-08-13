package com.andb.apps.lyrical.components.widgets

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.theme.LyricalPalette
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.outsetShadow
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.hover
import com.varabyte.kobweb.silk.components.style.toModifier

val ButtonStyle by ComponentStyle {
    base {
        Modifier
            .outsetShadow()
    }
}

val PrimaryButtonVariant by ButtonStyle.addVariant {
    base {
        Modifier.backgroundColor(LyricalTheme.paletteFrom(colorMode).background)
    }
    hover {
        Modifier.backgroundColor(LyricalTheme.paletteFrom(colorMode).backgroundDark)
    }
}

@Composable
fun Button(
    icon: (@Composable () -> Unit)?,
    text: String?,
    modifier: Modifier = Modifier,
    palette: LyricalPalette = LyricalTheme.palette,
) {
    org.jetbrains.compose.web.dom.Button(
        modifier
            .then(ButtonStyle.toModifier())
            .borderRadius(LyricalTheme.Radii.LG)
            .padding(topBottom = LyricalTheme.Spacing.SM, leftRight = LyricalTheme.Spacing.LG)
            .toAttrs()
    ) {
        Row(Modifier.gap(LyricalTheme.Spacing.MD)) {
            if (icon != null) {
                icon()
            }
            if (text != null) {
                Subtitle(text)
            }
        }
    }
}