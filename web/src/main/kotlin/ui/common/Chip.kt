package ui.common

import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv
import styled.styledP
import ui.theme

fun RBuilder.Chip(text: String, backgroundColor: Color = theme.backgroundCard, textColor: Color = theme.onBackgroundSecondary, fontSize: LinearDimension = 24.px, icon: Icon? = null, onClick: (() -> Unit)? = null) = child(chip) {
    attrs {
        this.text = text
        this.backgroundColor = backgroundColor
        this.textColor = textColor
        this.fontSize = fontSize
        this.icon = icon
        this.onClick = onClick
    }
}

external interface ChipProps : RProps {
    var icon: Icon?
    var text: String
    var backgroundColor: Color
    var textColor: Color
    var fontSize: LinearDimension
    var onClick: (() -> Unit)?
}

val chip = functionalComponent<ChipProps> { props ->
    styledDiv {
        css {
            backgroundColor = props.backgroundColor
            borderRadius = 32.px
            padding(vertical = 4.px, horizontal = 12.px)
            color = theme.onBackground
            if (props.onClick != null) {
                cursor = Cursor.pointer
            }
        }
        attrs {
            if (props.onClick != null) {
                onClickFunction = {
                    props.onClick?.invoke()
                }
            }
        }
        styledP {
            css {
                fontSize = props.fontSize
                color = props.textColor
            }
            +props.text
        }
    }
}