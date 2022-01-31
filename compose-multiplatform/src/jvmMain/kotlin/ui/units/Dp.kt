package compose.multiplatform.ui.unit

import androidx.compose.ui.unit.Dp as JDp

val Dp.implementation: JDp
    get() = JDp(value)