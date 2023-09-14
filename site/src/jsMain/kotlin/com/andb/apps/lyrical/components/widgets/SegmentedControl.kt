package com.andb.apps.lyrical.components.widgets

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.theme.InsetShadowStyle
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.OutsetShadowStyle
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent

@Composable
fun <T> SegmentedControl(
    options: List<T>,
    item: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .then(InsetShadowStyle.toModifier())
            .fillMaxWidth()
            .borderRadius(LyricalTheme.Radii.Circle)
            .backgroundColor(LyricalTheme.palette.backgroundDark)
            .alignItems(AlignItems.Center)
            .padding(LyricalTheme.Spacing.XXS),
    ) {
        options.forEach {
            item(it)
        }
    }
}

@Composable
fun <T> SegmentedControlItem(
    item: T,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    Row(
        modifier = modifier
            .then(SegmentedControlItemStyle.toModifier())
            .flexGrow(1)
            .padding(topBottom = LyricalTheme.Spacing.XS, leftRight = LyricalTheme.Spacing.SM)
            .justifyContent(JustifyContent.Center)
            .then(when(isSelected) {
                true -> Modifier
                    .backgroundColor(LyricalTheme.Colors.accentPalette.background)
                    .color(LyricalTheme.Colors.accentPalette.contentPrimary)
                    .then(OutsetShadowStyle.toModifier())
                false -> Modifier
                    .color(LyricalTheme.palette.contentSecondary)
            })
    ) {
        content(item)
    }
}


val SegmentedControlItemStyle by ComponentStyle.base {
    Modifier
        .flexGrow(1)
        .borderRadius(LyricalTheme.Radii.Circle)
        .cursor(Cursor.Pointer)
}