package compose.multiplatform.foundation.layout

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Arrangement
import compose.multiplatform.ui.SpacedArrangement
import org.jetbrains.compose.common.internal.modifierWrapper
import compose.multiplatform.ui.Alignment
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.asAttributeBuilderApplier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.ui.Styles

@Composable
actual fun Column(
    modifier: Modifier,
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable () -> Unit
) = modifierWrapper(modifier) {
    Div(
        attrs = {
            classes(Styles.columnClass)
            style {
                when (verticalArrangement) {
                    Arrangement.Top -> justifyContent(JustifyContent.FlexStart)
                    Arrangement.Center -> justifyContent(JustifyContent.Center)
                    Arrangement.Bottom -> justifyContent(JustifyContent.FlexEnd)
                    Arrangement.SpaceAround -> justifyContent(JustifyContent.SpaceAround)
                    Arrangement.SpaceBetween -> justifyContent(JustifyContent.SpaceBetween)
                    Arrangement.SpaceEvenly -> justifyContent(JustifyContent.SpaceEvenly)
                    is SpacedArrangement -> {
                        val direction = when (verticalArrangement) {
                            is SpacedArrangement.Aligned -> when(verticalArrangement.alignment) {
                                Alignment.Top -> JustifyContent.FlexStart
                                Alignment.CenterVertically -> JustifyContent.Center
                                else -> JustifyContent.FlexEnd
                            }
                            else -> JustifyContent.FlexStart
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
