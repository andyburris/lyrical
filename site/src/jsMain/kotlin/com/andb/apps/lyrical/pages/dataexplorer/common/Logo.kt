package com.andb.apps.lyrical.pages.dataexplorer.common

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.components.widgets.phosphor.IconStyle
import com.andb.apps.lyrical.components.widgets.phosphor.PhBinoculars
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*

@Composable
fun Logo() {
    Row(
        modifier = Modifier
            .borderRadius(LyricalTheme.Radii.MD)
            .size(LyricalTheme.Size.Button.SM)
            .backgroundColor(LyricalTheme.Colors.accentPalette.background),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PhBinoculars(
            style = IconStyle.BOLD,
            modifier = Modifier.color(LyricalTheme.Colors.accentPalette.contentPrimary)
        )
    }
}

@Composable
fun LogoMark() {
    Row(
        modifier = Modifier.gap(LyricalTheme.Spacing.MD),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Logo()
        Subtitle("Spotify Data Explorer")
    }
}