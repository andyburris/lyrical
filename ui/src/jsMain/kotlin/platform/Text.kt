package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.unit.TextUnit
import org.jetbrains.compose.common.ui.unit.TextUnitType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Span
import styles.text.FontFamily
import styles.text.FontStyle
import styles.text.TextOverflow
import styles.text.TextStyle
import kotlin.js.Promise

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
actual fun ActualText(
    text: String,
    modifier: Modifier,
    style: TextStyle?,
    color: Color?,
    maxLines: Int,
    overflow: TextOverflow,
) {
    Span(
        attrs = {
            style {
                if (style != null) {
                    applyTextStyle(style)
                }
                maxLines(maxLines)
                textOverflow(overflow)
                if (color != null) {
                    color(rgb(color.red, color.blue, color.green)) //TODO: replace with rgba, Color.implementation
                }
            }
        },
        content = {
            org.jetbrains.compose.web.dom.Text(text)
        }
    )
}

@OptIn(ExperimentalComposeWebWidgetsApi::class)
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