package jetbrains.compose.common.shapes

import org.jetbrains.compose.common.ui.unit.Dp
import org.jetbrains.compose.common.ui.unit.dp

interface Shape

/**
 * A shape describing the rectangle with rounded corners.
 *
 * This shape will automatically mirror the corner sizes in [LayoutDirection.Rtl], use
 * [AbsoluteRoundedCornerShape] for the layout direction unaware version of this shape.
 *
 * @param topStart a size of the top start corner
 * @param topEnd a size of the top end corner
 * @param bottomEnd a size of the bottom end corner
 * @param bottomStart a size of the bottom start corner
 */
class RoundedCornerShape(
    val topStart: CornerSize,
    val topEnd: CornerSize,
    val bottomEnd: CornerSize,
    val bottomStart: CornerSize
) : Shape {


    fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize
    ) = RoundedCornerShape(
        topStart = topStart,
        topEnd = topEnd,
        bottomEnd = bottomEnd,
        bottomStart = bottomStart
    )

    override fun toString(): String {
        return "RoundedCornerShape(topStart = $topStart, topEnd = $topEnd, bottomEnd = " +
                "$bottomEnd, bottomStart = $bottomStart)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RoundedCornerShape) return false

        if (topStart != other.topStart) return false
        if (topEnd != other.topEnd) return false
        if (bottomEnd != other.bottomEnd) return false
        if (bottomStart != other.bottomStart) return false

        return true
    }

    override fun hashCode(): Int {
        var result = topStart.hashCode()
        result = 31 * result + topEnd.hashCode()
        result = 31 * result + bottomEnd.hashCode()
        result = 31 * result + bottomStart.hashCode()
        return result
    }
}

/**
 * Circular [Shape] with all the corners sized as the 50 percent of the shape size.
 */
val CircleShape = RoundedCornerShape(50)

/**
 * Creates [RoundedCornerShape] with the same size applied for all four corners.
 * @param corner [CornerSize] to apply.
 */
fun RoundedCornerShape(corner: CornerSize) =
    RoundedCornerShape(corner, corner, corner, corner)

/**
 * Creates [RoundedCornerShape] with the same size applied for all four corners.
 * @param size Size in [Dp] to apply.
 */
fun RoundedCornerShape(size: Dp) = RoundedCornerShape(CornerSize(size))

/**
 * Creates [RoundedCornerShape] with the same size applied for all four corners.
 * @param percent Size in percents to apply.
 */
fun RoundedCornerShape(percent: Int) =
    RoundedCornerShape(CornerSize(percent))

/**
 * Creates [RoundedCornerShape] with sizes defined in [Dp].
 */
fun RoundedCornerShape(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp
) = RoundedCornerShape(
    topStart = CornerSize(topStart),
    topEnd = CornerSize(topEnd),
    bottomEnd = CornerSize(bottomEnd),
    bottomStart = CornerSize(bottomStart)
)

/**
 * Creates [RoundedCornerShape] with sizes defined in percents of the shape's smaller side.
 *
 * @param topStartPercent The top start corner radius as a percentage of the smaller side, with a
 * range of 0 - 100.
 * @param topEndPercent The top end corner radius as a percentage of the smaller side, with a
 * range of 0 - 100.
 * @param bottomEndPercent The bottom end corner radius as a percentage of the smaller side,
 * with a range of 0 - 100.
 * @param bottomStartPercent The bottom start corner radius as a percentage of the smaller side,
 * with a range of 0 - 100.
 */
fun RoundedCornerShape(
    /*@IntRange(from = 0, to = 100)*/
    topStartPercent: Int = 0,
    /*@IntRange(from = 0, to = 100)*/
    topEndPercent: Int = 0,
    /*@IntRange(from = 0, to = 100)*/
    bottomEndPercent: Int = 0,
    /*@IntRange(from = 0, to = 100)*/
    bottomStartPercent: Int = 0
) = RoundedCornerShape(
    topStart = CornerSize(topStartPercent),
    topEnd = CornerSize(topEndPercent),
    bottomEnd = CornerSize(bottomEndPercent),
    bottomStart = CornerSize(bottomStartPercent)
)
