package com.andb.apps.lyrical.components.widgets

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.OutsetShadowString
import com.andb.apps.lyrical.theme.OutsetShadowStyle
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.boxShadow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.Input
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.boxSizing
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.RangeInput

@Composable
fun Slider(
    currentValue: Int,
    possibleRange: IntRange,
    modifier: Modifier = Modifier,
    step: Int = 1,
    onValueChange: (Int) -> Unit,
) {
    RangeInput(
        value = currentValue,
        min = possibleRange.first,
        max = possibleRange.endInclusive,
        step = step,
        attrs = SliderStyle.toModifier()
            .then(modifier)
            .toAttrs {
                onInput {
                    val newValue = it.value
                    if (newValue is Int && newValue in possibleRange) {
                        onValueChange(newValue)
                    }
                }
            }
    )
}

val SliderStyle by ComponentStyle {
    base {
        Modifier
            .styleModifier {
                property("-webkit-appearance", "none")
                property("appearance", "none")
            }
            .cursor(Cursor.Pointer)
            .backgroundColor(Color.transparent)
    }
    cssRule("::-webkit-slider-thumb") {
        Modifier
            .cursor(Cursor.Grab)
            .margin(top = (-9).px)
            .styleModifier {
                boxShadow(OutsetShadowString)
            }
            .border(0.px)
            .size(24.px)
            .borderRadius(LyricalTheme.Radii.Circle)
            .backgroundColor(LyricalTheme.Colors.accentPalette.backgroundDark)
            .styleModifier {
                property("-webkit-appearance", "none")
                property("appearance", "none")
            }
    }

//    cssRule("::-moz-range-thumb") {
//        Modifier
//            .styleModifier {
//                boxShadow(OutsetShadowString)
//            }
//            .border(0.px)
//            .size(24.px)
//            .borderRadius(LyricalTheme.Radii.Circle)
//            .backgroundColor(LyricalTheme.Colors.accentPalette.backgroundDark)
//    }
//
//    cssRule("::-moz-range-track") {
//        Modifier
//            .height(6.px)
//            .borderRadius(LyricalTheme.Radii.Circle)
//            .backgroundColor(LyricalTheme.paletteFrom(colorMode).backgroundDark)
//    }

    cssRule("::-webkit-slider-runnable-track") {
        Modifier
            .height(6.px)
            .borderRadius(LyricalTheme.Radii.Circle)
            .backgroundColor(LyricalTheme.paletteFrom(colorMode).backgroundDark)
    }
}
