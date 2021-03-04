package ui.khakra

import emptyRecord
import kotlinext.js.Record
import kotlinext.js.asJsObject
import kotlinext.js.get
import kotlinext.js.set
import plusAssign
import recordOf

fun createTheme(block: ThemeScope.() -> Unit): Record<String, Any> {
    val themeScope = ThemeScope()
    block.invoke(themeScope)
    return themeScope.jsObject
}

class ThemeScope {
    internal val jsObject: Record<String, Any> = emptyRecord()
    fun borders(block: StyleScope.() -> Unit) = jsObject.styleBlock("borders", block)
    fun breakpoints(block: ValueScope.() -> Unit) = jsObject.valueBlock("breakpoints", block)
    fun colors(block: StyleScope.() -> Unit) = jsObject.styleBlock("colors", block)
    fun config(block: ConfigScope.() -> Unit) {
        val configScope = ConfigScope()
        block.invoke(configScope)
        jsObject["config"] = recordOf("useSystemColorMode" to configScope.useSystemColorMode, "initialColorMode" to if (configScope.initialColorMode == ColorMode.Light) "light" else "dark")
    }
    var direction: Direction get() = if (jsObject["direction"] == "rtl") Direction.RTL else Direction.LTR
        set(value) { jsObject["direction"] = if (value == Direction.LTR) "ltr" else "rtl" }
    fun radii(block: StyleScope.() -> Unit) = jsObject.styleBlock("radii", block)
    fun shadows(block: StyleScope.() -> Unit) = jsObject.styleBlock("shadows", block)
    fun space(block: StyleScope.() -> Unit) = jsObject.styleBlock("space", block)
    fun sizes(block: StyleScope.() -> Unit) = jsObject.styleBlock("sizes", block)
    fun globalStyles(block: StyleScope.() -> Unit) {
        val styles = emptyRecord<Any>().also { it.styleBlock("global", block) }
        jsObject["styles"] = styles
    }
    fun transition(block: TransitionScope.() -> Unit) {
        val transitionScope = TransitionScope()
        block.invoke(transitionScope)
        jsObject["config"] = transitionScope.jsObject
    }
    fun typography(block: TypographyScope.() -> Unit) {
        val typographyScope = TypographyScope()
        block.invoke(typographyScope)
        jsObject += typographyScope.jsObject
    }
    fun zIndices(block: ValueScope.() -> Unit) = jsObject.valueBlock("zIndices", block)
    fun layerStyles(block: ObjectScope.() -> Unit) = jsObject.objectBlock("layerStyles", block)
    fun textStyles(block: ObjectScope.() -> Unit) = jsObject.objectBlock("textStyles", block)
    fun components(block: ComponentListScope.() -> Unit) {
        jsObject["components"] = ComponentListScope().apply(block).jsObject
    }
}

/**Scope that allows string to string values, string to breakpoint array values, reactive function values, and subobject values**/
class StyleScope {
    internal val jsObject: Record<String, Any> = emptyRecord()
    infix fun String.to(value: String) { jsObject[this] = value }
    infix fun String.toBreakpoints(values: List<String>) { jsObject[this] = values.asJsObject() }
    infix fun String.toReactive(block: (props: StyleProps) -> Unit) {
        jsObject[this] = { props: Record<String, Any> ->
            val colorMode = if(props["colorMode"] as String == "light") ColorMode.Light else ColorMode.Dark
            StyleProps(colorMode).apply(block)
        }
    }
    infix fun String.toObject(block: StyleScope.() -> Unit) { jsObject[this] = StyleScope().apply(block).jsObject }
}

/**Scope that only allows string to string values**/
class ValueScope {
    internal val jsObject: Record<String, Any> = emptyRecord()
    infix fun String.to(value: String) { jsObject[this] = value }
}

/**Scope that only allows string to string values**/
class ObjectScope {
    internal val jsObject: Record<String, Any> = emptyRecord()
    infix fun String.toObject(block: StyleScope.() -> Unit) { jsObject[this] = StyleScope().apply(block).jsObject }
}
data class StyleProps(val colorMode: ColorMode)
enum class ColorMode { Light, Dark }
enum class Direction { LTR, RTL }

class ConfigScope {
    var useSystemColorMode: Boolean = false
    var initialColorMode: ColorMode = ColorMode.Light
}

class ComponentListScope {
    internal val jsObject: Record<String, Any> = emptyRecord()
    infix fun String.toComponent(block: ComponentScope.() -> Unit) { jsObject[this] = ComponentScope().apply(block).jsObject }
}

class ComponentScope {
    internal val jsObject: Record<String, Any> = emptyRecord()
    fun baseStyle(block: StyleScope.() -> Unit) = jsObject.styleBlock("baseStyle", block)
    fun sizes(block: ObjectScope.() -> Unit) = jsObject.objectBlock("sizes", block)
    fun variants(block: ObjectScope.() -> Unit) = jsObject.objectBlock("variants", block)
    fun defaultProps(block: ComponentDefaultScope.() -> Unit) {
        val componentDefaultScope = ComponentDefaultScope().apply(block)
        jsObject["defaultProps"] = mapOf("size" to componentDefaultScope.size, "variant" to componentDefaultScope.variant).filter { it.value != null }.asJsObject()
    }
}

class ComponentDefaultScope {
    var size: String? = null
    var variant: String? = null
}

class TransitionScope {
    internal val jsObject: Record<String, Any> = emptyRecord()
    private fun valueBlock(label: String, block: ValueScope.() -> Unit) { jsObject[label] = ValueScope().apply(block).jsObject }
    fun property(block: ValueScope.() -> Unit) = valueBlock("property", block)
    fun easing(block: ValueScope.() -> Unit) = valueBlock("easing", block)
    fun duration(block: ValueScope.() -> Unit) = valueBlock("duration", block)
}

class TypographyScope {
    internal val jsObject: Record<String, Any> = emptyRecord()
    private fun valueBlock(label: String, block: ValueScope.() -> Unit) { jsObject[label] = ValueScope().apply(block).jsObject }
    fun letterSpacings(block: ValueScope.() -> Unit) = valueBlock("letterSpacings", block)
    fun lineHeights(block: ValueScope.() -> Unit) = valueBlock("lineHeights", block)
    fun fontWeights(block: ValueScope.() -> Unit) = valueBlock("fontWeights", block)
    fun fonts(block: ValueScope.() -> Unit) = valueBlock("fonts", block)
    fun fontSizes(block: ValueScope.() -> Unit) = valueBlock("fontSizes", block)
}

private fun <V: Any> Record<String, V>.styleBlock(label: String, block: StyleScope.() -> Unit) { this[label] = StyleScope().apply(block).jsObject }
private fun <V: Any> Record<String, V>.valueBlock(label: String, block: ValueScope.() -> Unit) { this[label] = ValueScope().apply(block).jsObject }
private fun <V: Any> Record<String, V>.objectBlock(label: String, block: ObjectScope.() -> Unit) { this[label] = ObjectScope().apply(block).jsObject }