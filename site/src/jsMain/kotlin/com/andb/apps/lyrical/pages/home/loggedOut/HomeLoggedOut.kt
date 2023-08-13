package com.andb.apps.lyrical.pages.home.loggedOut

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.gap

@Composable
fun HomeLoggedOut() {

}

@Composable
private fun LoginCard(modifier: Modifier = Modifier, onLogin: () -> Unit) {
    Column(
        modifier = modifier
            .background(LyricalTheme.palette.backgroundDark)
            .borderRadius(LyricalTheme.Radii.XL)
            .gap(LyricalTheme.Spacing.XL),
    ) {
        Subtitle("Log in to play Lyrical with your own playlists")

    }
}