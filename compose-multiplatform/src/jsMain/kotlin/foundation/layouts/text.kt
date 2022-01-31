package compose.multiplatform.foundation

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.implementation
import compose.multiplatform.ui.Color
import org.jetbrains.compose.common.internal.modifierWrapper
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.text.FontFamily
import compose.multiplatform.ui.text.FontStyle
import compose.multiplatform.ui.text.TextOverflow
import compose.multiplatform.ui.text.TextStyle
import compose.multiplatform.ui.unit.TextUnit
import compose.multiplatform.ui.unit.TextUnitType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Span
import kotlin.js.Promise

@Composable
actual fun Text(
    text: String,
    modifier: Modifier,
    style: TextStyle?,
    color: Color?,
    maxLines: Int,
    overflow: TextOverflow,
) = modifierWrapper(modifier) {
    Span(
        attrs = {
            style {
                if (style != null) {
                    applyTextStyle(style)
                }
                maxLines(maxLines)
                textOverflow(overflow)
                if (color != null) {
                    color(color.implementation)
                }
            }
        },
        content = {
            org.jetbrains.compose.web.dom.Text(text)
        }
    )
}

fun TextUnit.cssValue(): CSSSizeValue<*> = when (this.unitType) {
    TextUnitType.Unspecified -> this.value.px
    TextUnitType.Em -> this.value.em
    TextUnitType.Sp -> this.value.px
}

val FontStyle.name get() = when(this) {
    FontStyle.Italic -> "italic"
    else -> "normal"
}

fun StyleBuilder.applyTextStyle(style: TextStyle) {
    fontSize(style.fontSize.cssValue())
    style.fontWeight?.let { this.fontWeight(it.weight) }
    style.fontStyle?.let { this.fontStyle(it.name) }
    this.property("letter-spacing", style.letterSpacing.cssValue())
    this.property("line-height", style.lineHeight.cssValue())
    style.fontFamily?.let { this.fontFamily(it.alias) }
}

fun StyleBuilder.maxLines(lines: Int) {
    property("word-wrap", "break-word")
    property("overflow-wrap", "anywhere")
    property("display", "-webkit-box")
    property("-webkit-line-clamp", lines.toString())
    property("-webkit-box-orient", "vertical")
}

fun StyleBuilder.textOverflow(overflow: TextOverflow) {
    when(overflow) {
        TextOverflow.Clip -> {
            overflow("hidden")
            property("text-overflow", "clip")
        }
        TextOverflow.Ellipsis -> {
            overflow("hidden")
            property("text-overflow", "ellipsis")
        }
        TextOverflow.Visible -> {
            overflow("visible")
        }
    }
}

external class FontFace(family: String, src: String) {
    fun load(): Promise<FontFace>
}

fun FontFamily.toFontFace() = FontFace(alias, filePath)