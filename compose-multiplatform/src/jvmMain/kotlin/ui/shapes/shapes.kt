@file:JvmName("PlatformShapes")
package compose.multiplatform.ui.shape

import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Shape as JShape
import androidx.compose.foundation.shape.CircleShape as JCircleShape
import androidx.compose.foundation.shape.RoundedCornerShape as JRoundedCornerShape

val Shape.implementation: JShape
    get() = when (this) {
        is RoundedCornerShape -> when(this.topStart) {
            is PercentCornerSize -> JRoundedCornerShape(
                topStartPercent = this.topStart.percent.toInt(),
                topEndPercent = (this.topEnd as PercentCornerSize).percent.toInt(),
                bottomStartPercent = (this.bottomStart as PercentCornerSize).percent.toInt(),
                bottomEndPercent = (this.bottomEnd as PercentCornerSize).percent.toInt(),
            )
            is DpCornerSize -> JRoundedCornerShape(
                topStart = (this.topStart as DpCornerSize).size.value.dp,
                topEnd = (this.topEnd as DpCornerSize).size.value.dp,
                bottomStart = (this.bottomStart as DpCornerSize).size.value.dp,
                bottomEnd = (this.bottomEnd as DpCornerSize).size.value.dp,
            )
            else -> throw ClassCastException("Currently supporting only PercentCornerSize and DpCornerSize")
        }
        else -> throw ClassCastException("Currently supporting only RoundedCornerShape")
    }
