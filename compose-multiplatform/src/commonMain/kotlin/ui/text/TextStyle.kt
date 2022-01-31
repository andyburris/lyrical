package compose.multiplatform.ui.text

import compose.multiplatform.ui.unit.TextUnit
import compose.multiplatform.ui.unit.TextUnitType

data class TextStyle(
    val fontSize: TextUnit = TextUnit.Unspecified,
    val fontWeight: FontWeight? = null,
    val fontStyle: FontStyle? = null,
    val fontFamily: FontFamily? = null,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
    val lineHeight: TextUnit = TextUnit.Unspecified,
)

val Double.sp: TextUnit
    get() = TextUnit(this.toFloat(), TextUnitType.Sp)