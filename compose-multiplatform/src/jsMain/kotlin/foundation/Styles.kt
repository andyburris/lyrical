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

    val composeWebArrangementHorizontalStart by style {
        justifyContent(JustifyContent.FlexStart)
    }

    val composeWebArrangementHorizontalEnd by style {
        justifyContent(JustifyContent.FlexEnd)
    }

    val composeWebAlignmentVerticalTop by style {
        alignItems(AlignItems.FlexStart)
    }

    val composeWebAlignmentVerticalCenter by style {
        alignItems(AlignItems.Center)
    }

    val composeWebAlignmentVerticalBottom by style {
        alignItems(AlignItems.FlexEnd)
    }
}
