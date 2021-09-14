package styles.text

import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.unit.TextUnit
import org.jetbrains.compose.common.ui.unit.TextUnitType

@OptIn(ExperimentalComposeWebWidgetsApi::class)
data class TextStyle(
    val fontSize: TextUnit = TextUnit.Unspecified,
    val fontWeight: FontWeight? = null,
    val fontStyle: FontStyle? = null,
    val fontFamily: FontFamily? = null,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
    val lineHeight: TextUnit = TextUnit.Unspecified,
)

@OptIn(ExperimentalComposeWebWidgetsApi::class)
val Double.sp: TextUnit
    get() = TextUnit(this.toFloat(), TextUnitType.Sp)