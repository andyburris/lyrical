package org.jetbrains.compose.common.foundation.layout

import org.jetbrains.compose.common.ui.implementation
import org.jetbrains.compose.common.ui.unit.implementation
import androidx.compose.foundation.layout.Arrangement as JArrangement

val Arrangement.Vertical.implementation: JArrangement.Vertical
    get() = when(this) {
        Arrangement.Top -> JArrangement.Top
        Arrangement.Bottom -> JArrangement.Bottom
        is Arrangement.HorizontalOrVertical -> this.implementation
        is SpacedArrangement.Vertical -> JArrangement.spacedBy(this.space.implementation, this.alignment.implementation)
        else -> throw ClassCastException("Other values not supported")
    }

val Arrangement.Horizontal.implementation: JArrangement.Horizontal
    get() = when (this) {
        Arrangement.Start -> JArrangement.Start
        Arrangement.End -> JArrangement.End
        is Arrangement.HorizontalOrVertical -> this.implementation
        is SpacedArrangement.Horizontal -> JArrangement.spacedBy(this.space.implementation, this.alignment.implementation)
        else -> throw ClassCastException("Other values not supported")
    }


val Arrangement.HorizontalOrVertical.implementation: JArrangement.HorizontalOrVertical
    get() = when(this) {
        Arrangement.Center -> JArrangement.Center
        Arrangement.SpaceAround -> JArrangement.SpaceAround
        Arrangement.SpaceBetween -> JArrangement.SpaceBetween
        Arrangement.SpaceEvenly -> JArrangement.SpaceEvenly
        else -> throw ClassCastException("Other values not supported")
    }