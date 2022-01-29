package org.jetbrains.compose.common.foundation.layout

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.internal.ModifierWrapperLayout
import org.jetbrains.compose.common.ui.Alignment
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.ui.Styles

private fun Arrangement.Horizontal.asClassName() = when (this) {
    Arrangement.End -> Styles.composeWebArrangementHorizontalEnd
    else -> Styles.composeWebArrangementHorizontalStart
}

private fun Alignment.Vertical.asClassName() = when (this) {
    Alignment.Top -> Styles.composeWebAlignmentVerticalTop
    Alignment.CenterVertically -> Styles.composeWebAlignmentVerticalCenter
    else -> Styles.composeWebAlignmentVerticalBottom
}

@Composable
internal actual fun Row(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable () -> Unit
) {
    ModifierWrapperLayout(modifier) {
        Div(
            attrs = {
                classes(
                    *arrayOf(
                        Styles.rowClass,
                        horizontalArrangement.asClassName(),
                        verticalAlignment.asClassName()
                    )
                )
                style {
                    when(horizontalArrangement) {
                        Arrangement.Start -> justifyContent(JustifyContent.FlexStart)
                        Arrangement.Center -> justifyContent(JustifyContent.Center)
                        Arrangement.End -> justifyContent(JustifyContent.FlexEnd)
                        Arrangement.SpaceAround -> justifyContent(JustifyContent.SpaceAround)
                        Arrangement.SpaceBetween -> justifyContent(JustifyContent.SpaceBetween)
                        Arrangement.SpaceEvenly -> justifyContent(JustifyContent.SpaceEvenly)
                        is SpacedArrangement -> {
                            val direction = when(horizontalArrangement.alignment) {
                                Alignment.Start -> JustifyContent.FlexStart
                                Alignment.CenterHorizontally -> JustifyContent.Center
                                else -> JustifyContent.FlexEnd
                            }
                            justifyContent(direction)
                            property("gap", horizontalArrangement.space.value.px)
                        }
                    }
                    when(verticalAlignment) {
                        Alignment.Top -> alignContent(AlignContent.Start)
                        Alignment.CenterVertically -> alignContent(AlignContent.Center)
                        Alignment.Bottom -> alignContent(AlignContent.End)
                    }
                }
            }
        ) {
            content()
        }
    }
}
