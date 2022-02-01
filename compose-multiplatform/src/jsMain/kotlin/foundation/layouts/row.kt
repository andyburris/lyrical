package compose.multiplatform.foundation.layout

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.modifiers.RowScope
import compose.multiplatform.foundation.modifiers.RowScopeImpl
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.SpacedArrangement
import org.jetbrains.compose.common.internal.modifierWrapper
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
actual fun Row(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable RowScope.() -> Unit
) = modifierWrapper(modifier) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
                width(100.percent)
                when (horizontalArrangement) {
                    Arrangement.Start -> justifyContent(JustifyContent.FlexStart)
                    Arrangement.Center -> justifyContent(JustifyContent.Center)
                    Arrangement.End -> justifyContent(JustifyContent.FlexEnd)
                    Arrangement.SpaceAround -> justifyContent(JustifyContent.SpaceAround)
                    Arrangement.SpaceBetween -> justifyContent(JustifyContent.SpaceBetween)
                    Arrangement.SpaceEvenly -> justifyContent(JustifyContent.SpaceEvenly)
                    is SpacedArrangement -> {
                        val direction = when (horizontalArrangement) {
                            is SpacedArrangement.Aligned -> when(horizontalArrangement.alignment) {
                                Alignment.Start -> JustifyContent.FlexStart
                                Alignment.CenterHorizontally -> JustifyContent.Center
                                else -> JustifyContent.FlexEnd
                            }
                            else -> JustifyContent.FlexStart
                        }
                        justifyContent(direction)
                        property("gap", horizontalArrangement.space.value.px)
                    }
                }
                when (verticalAlignment) {
                    Alignment.Top -> alignContent(AlignContent.Start)
                    Alignment.CenterVertically -> alignContent(AlignContent.Center)
                    Alignment.Bottom -> alignContent(AlignContent.End)
                }
            }
        }
    ) {
        content(RowScopeImpl())
    }
}
