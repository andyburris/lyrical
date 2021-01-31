package ui.landing

import Screen
import Size
import flexColumn
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.functionalComponent
import react.child
import react.dom.button
import react.dom.h1
import react.dom.p
import styled.css
import styled.styledImg
import styled.styledP
import ui.theme
import kotlin.random.Random

fun RBuilder.LandingScreen(onAuthenticate: (Action.Authenticate) -> Unit) = child(landingScreen) {
    attrs {
        this.onAuthenticate = onAuthenticate
    }
}
external interface LandingProps : RProps {
    var onAuthenticate: (Action.Authenticate) -> Unit
}
val landingScreen = functionalComponent<LandingProps> { props ->
    Screen {
        flexColumn(gap = 64.px) {
            flexColumn(gap = 16.px) {
                styledImg (src = "/assets/Quotes.svg"){
                    css { Size(72.px, 36.px) }
                }
                h1 { +"How well do you know your playlists?" }
                p { +"Guess songs from your playlists from random lyrics. Play by yourself or against your friends." }
            }
            flexColumn(gap = 16.px) {
                button {
                    +"LOG IN WITH SPOTIFY"
                    attrs.onClickFunction = {
                        props.onAuthenticate.invoke(Action.Authenticate(Random.nextInt().toString()))
                    }
                }
                styledP {
                    css {
                        color = theme.onBackgroundPlaceholder
                        fontSize = 18.px
                        fontWeight = FontWeight.w600
                    }
                    +"Lyrical only has access to read your username and playlists"
                }
            }
        }
    }
}