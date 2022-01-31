package compose.multiplatform.ui.shape

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import compose.multiplatform.ui.unit.Dp

/**
 * Defines size of a corner in pixels. For example for rounded shape it can be a corner radius.
 */
@Immutable
interface CornerSize

/**
 * Creates [CornerSize] with provided size.
 * @param size the corner size defined in [Dp].
 */
@Stable
fun CornerSize(size: Dp): CornerSize = DpCornerSize(size)

data class DpCornerSize(val size: Dp) : CornerSize {
    override fun toString(): String = "CornerSize(size = ${size.value}.dp)"
}


/**
 * Creates [CornerSize] with provided size.
 * @param percent the corner size defined in percents of the shape's smaller side.
 * Can't be negative or larger then 100 percents.
 */
@Stable
fun CornerSize(/*@IntRange(from = 0, to = 100)*/ percent: Int): CornerSize =
    PercentCornerSize(percent.toFloat())

/**
 * Creates [CornerSize] with provided size.
 * @param percent the corner size defined in float percents of the shape's smaller side.
 * Can't be negative or larger then 100 percents.
 */
data class PercentCornerSize(
    /*@FloatRange(from = 0.0, to = 100.0)*/
    val percent: Float
) : CornerSize {
    init {
        if (percent < 0 || percent > 100) {
            throw IllegalArgumentException("The percent should be in the range of [0, 100]")
        }
    }

    override fun toString(): String = "CornerSize(size = $percent%)"
}

/**
 * [CornerSize] always equals to zero.
 */
@Stable
val ZeroCornerSize: CornerSize = object : CornerSize {
    override fun toString(): String = "ZeroCornerSize"
}
