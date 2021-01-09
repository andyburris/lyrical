package ui

import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.child
import react.dom.button
import react.dom.h1
import react.functionalComponent
import styled.css
import styled.styledDiv
import styled.styledP

fun RBuilder.EndScreen(state: State.GameState.End, onRestart: () -> Unit) = child(endScreen) {
    attrs {
        this.state = state
        this.onRestart = onRestart
    }
}

external interface EndScreenProps : RProps {
    var state: State.GameState.End
    var onRestart: () -> Unit
}
val endScreen = functionalComponent<EndScreenProps> { props ->
    styledDiv {
        css {
            width = 100.vw
            height = 100.vh
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            padding(vertical = 128.px, horizontal = 196.px)
            boxSizing = BoxSizing.borderBox
        }
        styledP {
            css {
                color = theme.onBackgroundSecondary
            }
            +"SCORE"
        }
        h1 {
            val score = "${props.state.data.points}/${props.state.data.questions.size}"
            +score
        }
        button {
            +"Restart".toUpperCase()
            attrs {
                onClickFunction = {
                    props.onRestart.invoke()
                }
            }
        }
    }
}