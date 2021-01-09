package ui

import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledH1

fun RBuilder.LoginScreen(onLoginClick: () -> Unit) = child(login) {
    attrs {
        this.onLoginClick = onLoginClick
    }
}
external interface LoginProps : RProps {
    var onLoginClick: () -> Unit
}
val login = functionalComponent<LoginProps> { props ->
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
            +"Lyrical"
        }
        styledButton {
            css {
                marginTop = 32.px
            }
            attrs {
                onClickFunction = {
                    props.onLoginClick.invoke()
                }
            }
            +"Login with Spotify".toUpperCase()
        }
    }
}
