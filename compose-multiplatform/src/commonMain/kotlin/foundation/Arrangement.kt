package org.jetbrains.compose.common.foundation.layout

import androidx.compose.runtime.Stable
import org.jetbrains.compose.common.ui.Alignment
import org.jetbrains.compose.common.ui.unit.Dp
import org.jetbrains.compose.common.ui.unit.dp

object Arrangement {

    /**
     * Used to specify the horizontal arrangement of the layout's children in layouts like [Row].
     */
    @Stable
    interface Horizontal {
        /**
         * Spacing that should be added between any two adjacent layout children.
         */
        val spacing get() = 0.dp
    }

    /**
     * Used to specify the vertical arrangement of the layout's children in layouts like [Column].
     */
    interface Vertical {
        /**
         * Spacing that should be added between any two adjacent layout children.
         */
        val spacing get() = 0.dp
    }


    /**
     * Used to specify the horizontal arrangement of the layout's children in horizontal layouts
     * like [Row], or the vertical arrangement of the layout's children in vertical layouts like
     * [Column].
     */
    interface HorizontalOrVertical : Horizontal, Vertical {
        /**
         * Spacing that should be added between any two adjacent layout children.
         */
        override val spacing get() = 0.dp
    }

    val End = object : Horizontal {}
    val Start = object : Horizontal {}
    val Center = object : HorizontalOrVertical {}
    val Top = object : Vertical {}
    val Bottom = object : Vertical {}
    val SpaceAround = object : HorizontalOrVertical {}
    val SpaceBetween = object : HorizontalOrVertical {}
    val SpaceEvenly = object : HorizontalOrVertical {}

    fun spacedBy(space: Dp, alignment: Alignment.Horizontal = Alignment.Start): Horizontal = SpacedArrangement.Horizontal(space, alignment)
    fun spacedBy(space: Dp, alignment: Alignment.Vertical = Alignment.Top): Vertical = SpacedArrangement.Vertical(space, alignment)
}

sealed class SpacedArrangement : Arrangement.HorizontalOrVertical {
    abstract val space: Dp
    abstract val alignment: Alignment
    data class Horizontal(override val space: Dp, override val alignment: Alignment.Horizontal) : SpacedArrangement()
    data class Vertical(override val space: Dp, override val alignment: Alignment.Vertical) : SpacedArrangement()
}
