package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.unit.TextUnit
import org.jetbrains.compose.common.ui.unit.TextUnitType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Span
import styles.text.TextStyle

@Composable
actual fun Text(text: String, modifier: Modifier, style: TextStyle?, color: Color?) {
    Span(
        attrs = {
            style {
                fontSize(style.fontSize.cssValue())
                style.fontWeight?.let { this.property("font-weight", StylePropertyValue(it.weight)) }
                style.fontStyle?.let { this.property("font-style", StylePropertyValue(it.name.lowercase())) }
                this.property("letter-spacing", StylePropertyValue(style.letterSpacing.cssValue()))
                this.property("line-height", StylePropertyValue(style.lineHeight.cssValue()))
            }
        },
        content = {
            org.jetbrains.compose.web.dom.Text(text)
        }
    )
}

fun TextUnit.cssValue(): CSSSizeValue = when (this.unitType) {
    TextUnitType.Unspecified -> this.value.px
    TextUnitType.Em -> this.value.em
    TextUnitType.Sp -> this.value.px
}