package com.andb.apps.lyrical.components.widgets

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.theme.LyricalPalette
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.OutsetShadowStyle
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.*
import org.jetbrains.compose.web.css.*

val ButtonStyle by ComponentStyle {
    base {
        Modifier
            .border(0.px)
            .transition(CSSTransition("background", 150.ms, AnimationTimingFunction.EaseOut))
    }
}

val PrimaryButtonVariant by ButtonStyle.addVariant {
    base {
        Modifier
            .backgroundColor(LyricalTheme.paletteFrom(colorMode).background)
            .color(LyricalTheme.paletteFrom(colorMode).contentPrimary)
    }
    hover {
        Modifier
            .backgroundColor(LyricalTheme.paletteFrom(colorMode).backgroundDark)
    }
}

val AccentButtonVariant by ButtonStyle.addVariant {
    base {
        Modifier
            .backgroundColor(LyricalTheme.Colors.accentPalette.background)
            .color(LyricalTheme.Colors.accentPalette.contentPrimary)
    }
    hover {
        Modifier
            .backgroundColor(LyricalTheme.Colors.accentPalette.backgroundDark)
    }
}

val EnabledVariant by ButtonStyle.addVariantBase {
    Modifier.cursor(Cursor.Pointer)
}

val DisabledVariant by ButtonStyle.addVariantBase {
    Modifier.opacity(0.5)
}

@Composable
fun Button(
    text: String?,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    isEnabled: Boolean = true,
    palette: LyricalPalette = LyricalTheme.palette,
) {
    val colorVariant = when(palette) {
        LyricalTheme.Colors.accentPalette -> AccentButtonVariant
        else -> PrimaryButtonVariant
    }
    val enabledVariant = if (isEnabled) EnabledVariant else DisabledVariant
    org.jetbrains.compose.web.dom.Button(
        modifier
            .then(ButtonStyle.toModifier(colorVariant, enabledVariant))
            .then(OutsetShadowStyle.toModifier())
            .height(LyricalTheme.Size.Button.LG)
            .borderRadius(LyricalTheme.Radii.MD)
            .padding(topBottom = LyricalTheme.Spacing.SM, leftRight = LyricalTheme.Spacing.LG)
            .then(if (text == null) Modifier.width(LyricalTheme.Size.Button.LG) else Modifier)
            .toAttrs()
    ) {
        Row(
            modifier = Modifier
                .gap(LyricalTheme.Spacing.MD)
                .justifyContent(JustifyContent.Center)
                .alignItems(AlignItems.Center)
        ) {
            if (icon != null) {
                icon()
            }
            if (text != null) {
                Subtitle(text)
            }
        }
    }
}
