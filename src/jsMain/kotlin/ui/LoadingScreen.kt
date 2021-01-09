package ui

import kotlinx.css.*
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv
import styled.styledH1

fun RBuilder.LoadingScreen() = child(loading) {}

val loading = functionalComponent<RProps> {
    styledDiv {
        css {
            width = 100.vw
            height = 100.vh
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
            justifyContent = JustifyContent.center
        }
        styledH1 {
            +"Loading..."
        }
    }
}
