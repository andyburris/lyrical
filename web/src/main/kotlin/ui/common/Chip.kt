package ui.common

import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.button.ButtonProps
import com.github.mpetuska.khakra.colorMode.useColorModeValue
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv
import styled.styledP
import ui.khakra.onClick
import ui.theme

fun RBuilder.Chip(text: String, props: ButtonProps.() -> Unit = {}, onClick: (() -> Unit)? = null) {
    Button({
        size = "chip"
        variant = useColorModeValue("outline", "solidCard")
        if (onClick != null) this.onClick = onClick
        props()
    }) {
        +text
    }
}