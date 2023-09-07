package com.andb.apps.lyrical.pages.home.loggedOut

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.Body
import com.andb.apps.lyrical.components.widgets.Button
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.percent

@Composable
fun HomeLoggedOut(modifier: Modifier = Modifier, onLogin: () -> Unit) {
    LoginCard(onLogin = onLogin)
}

@Composable
private fun LoginCard(modifier: Modifier = Modifier, onLogin: () -> Unit) {
    Column(
        modifier = modifier
            .background(LyricalTheme.palette.backgroundDark)
            .borderRadius(LyricalTheme.Radii.XL)
            .gap(LyricalTheme.Spacing.XL)
            .padding(LyricalTheme.Spacing.XL)
            .overflow(Overflow.Hidden)
            .fillMaxWidth(),
    ) {
        Subtitle("Log in to play Lyrical with your own playlists")
        Row(Modifier.gap(LyricalTheme.Spacing.MD).fillMaxWidth()) {
            repeat(6) {
                PlaylistPlaceholder()
            }
        }
        Button(
            text = "Log in with Spotify",
            modifier = Modifier.fillMaxWidth().onClick { onLogin() },
            palette = LyricalTheme.Colors.accentPalette)
        Body(
            text = "Lyrical doesn’t collect your login information. All data is stored on your device.",
            color = LyricalTheme.palette.contentSecondary,
        )
    }
}

@Composable
fun PlaylistPlaceholder(modifier: Modifier = Modifier) {
    Column(modifier.gap(LyricalTheme.Spacing.SM)) {
        Box(
            modifier = Modifier
                .size(LyricalTheme.Size.PlaylistPlaceholder.Cover)
                .backgroundColor(LyricalTheme.palette.overlay)
                .borderRadius(LyricalTheme.Radii.SM)
        )
        Column(Modifier.gap(LyricalTheme.Spacing.XS)) {
            Box(
                modifier = Modifier
                    .width(LyricalTheme.Spacing.XXXL)
                    .height(LyricalTheme.Spacing.SM)
                    .backgroundColor(LyricalTheme.palette.overlay)
                    .borderRadius(LyricalTheme.Radii.Circle)
            )
            Box(
                modifier = Modifier
                    .width(LyricalTheme.Spacing.XXL)
                    .height(LyricalTheme.Spacing.SM)
                    .backgroundColor(LyricalTheme.palette.overlay)
                    .borderRadius(LyricalTheme.Radii.Circle)
            )
        }
    }
}