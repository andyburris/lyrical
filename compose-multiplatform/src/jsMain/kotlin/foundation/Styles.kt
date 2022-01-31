package org.jetbrains.compose.web.ui

import org.jetbrains.compose.web.css.*

object Styles : StyleSheet() {
    val columnClass by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
    }

    val textClass by style {
        display(DisplayStyle.Block)
        left(0.px)
    }

    val rowClass by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Row)
    }
}
