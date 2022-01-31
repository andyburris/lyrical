package org.jetbrains.compose.common.material

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.internal.modifierWrapper
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.attributes.InputType

@Composable
actual fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    modifier: Modifier,
) = modifierWrapper(modifier) {
    val stepCount = if (steps == 0) 100 else steps
    val step = (valueRange.endInclusive - valueRange.start) / stepCount

    Input(
        type = InputType.Range,
        attrs = {
            value(value.toString())
            attr("min", valueRange.start.toString())
            attr("max", valueRange.endInclusive.toString())
            attr("step", step.toString())
            onInput {
                onValueChange(it.value?.toFloat() ?: 0f)
            }
        }
    )
}
