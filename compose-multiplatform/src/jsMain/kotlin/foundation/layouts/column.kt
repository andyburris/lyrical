package org.jetbrains.compose.common.foundation.layout

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.internal.ModifierWrapperLayout
import org.jetbrains.compose.common.ui.Alignment
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.asAttributeBuilderApplier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.ui.Styles

@Composable
internal actual fun Column(
    modifier: Modifier,
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable () -> Unit
) {
    ModifierWrapperLayout(modifier) {
        Div(
            attrs = {
                classes(Styles.columnClass)
                apply(modifier.asAttributeBuilderApplier())
                style {
                    when (verticalArrangement) {
                        Arrangement.Top -> justifyContent(JustifyContent.FlexStart)
                        Arrangement.Center -> justifyContent(JustifyContent.Center)
                        Arrangement.Bottom -> justifyContent(JustifyContent.FlexEnd)
                        Arrangement.SpaceAround -> justifyContent(JustifyContent.SpaceAround)
                        Arrangement.SpaceBetween -> justifyContent(JustifyContent.SpaceBetween)
                        Arrangement.SpaceEvenly -> justifyContent(JustifyContent.SpaceEvenly)
                        is SpacedArrangement -> {
                            val direction = when (verticalArrangement.alignment) {
                                Alignment.Top -> JustifyContent.FlexStart
                                Alignment.CenterVertically -> JustifyContent.Center
                                else -> JustifyContent.FlexEnd
                            }
                            justifyContent(direction)
                            property("gap", verticalArrangement.space.value.px)
                        }
                    }
                    when (horizontalAlignment) {
                        Alignment.Start -> alignContent(AlignContent.Start)
                        Alignment.CenterHorizontally -> alignContent(AlignContent.Center)
                        Alignment.End -> alignContent(AlignContent.End)
                    }
                }
            }
        ) {
            content()
        }
    }
}
