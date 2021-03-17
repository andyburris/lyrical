package ui.khakra

import recordOf
import kotlinext.js.Record
import kotlinext.js.asJsObject
import kotlinext.js.get
import kotlinext.js.set
import plus
import plusAssign

fun createTheme(block: ThemeScope.() -> Unit): Record<String, Any> {
    val themeScope = ThemeScope()
    block.invoke(themeScope)
    return themeScope.jsObject
}

class ThemeScope {
    internal val jsObject: Record<String, Any> = recordOf()
    fun styleBlock(block: StyleScope.() -> Unit): StyleScope.() -> Unit = block
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
    fun space(block: StyleScope.() -> Unit) {
        jsObject.styleBlock("space", block)
        jsObject.styleBlock("sizes", block)
    }
    fun sizes(block: StyleScope.() -> Unit) = jsObject.styleBlock("sizes", block)
    fun globalStyles(block: StyleScope.(StyleProps) -> Unit) {
        val styles = recordOf<Any>().also { it.reactiveStyleBlock("global", block) }
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
    fun layerStyles(block: LayerScope.() -> Unit) = jsObject.layerBlock("layerStyles", block)
    fun textStyles(block: LayerScope.() -> Unit) = jsObject.layerBlock("textStyles", block)
    fun components(block: ComponentListScope.() -> Unit) {
        jsObject["components"] = ComponentListScope().apply(block).jsObject
    }
}

/**Scope that allows string to string values, string to breakpoint array values, reactive function values, and subobject values**/
open class StyleScope {
    internal val jsObject: Record<String, Any> = recordOf()
    infix fun String.to(value: String) { jsObject[this] = value }
    fun String.toBreakpoints(values: List<String>) { jsObject[this] = values.asJsObject() }
    fun String.toBreakpoints(vararg values: String) { jsObject[this] = values }
    infix fun String.toReactive(block: StyleScope.(props: StyleProps) -> Unit) {
        println("running toReactive")
        fun function(reallyLongName: GlobalStyleProps) : Record<String, Any> {
            println("in closure")
            println("props = $reallyLongName")
            println("reallyLongName = ${JSON.stringify(reallyLongName)}")
            println("props.colorMode = ${reallyLongName.colorMode}")
            /*val colorMode = if(props["colorMode"] as String == "light") ColorMode.Light else ColorMode.Dark
            println("after colorMode = $colorMode")
            val styleProps = StyleProps(colorMode)
            val scope = StyleScope()
            block.invoke(scope, styleProps)
            scope.jsObject*/
            return recordOf()
        }
        jsObject[this] = ::function
    }
    infix fun String.toObject(block: StyleScope.() -> Unit) { jsObject[this] = StyleScope().apply(block).jsObject }
}

external interface GlobalStyleProps {
    var colorScheme: String
    var colorMode: String
    var theme: Record<String, Any>
}
fun GlobalStyleProps.toStyleProps() = StyleProps(if(colorMode == "light") ColorMode.Light else ColorMode.Dark)

/**Scope that only allows string to string values**/
class ValueScope {
    internal val jsObject: Record<String, Any> = recordOf()
    infix fun String.to(value: String) { jsObject[this] = value }
}

/**Scope that only allows string to string values**/
class ObjectScope {
    internal val jsObject: Record<String, Any> = recordOf()
    infix fun String.toObject(block: StyleScope.() -> Unit) = jsObject.styleBlock(this, block)
    infix fun String.toReactive(block: StyleScope.(StyleProps) -> Unit) = jsObject.reactiveStyleBlock(this, block)
}
data class StyleProps(val colorMode: ColorMode)
enum class ColorMode { Light, Dark }
fun ColorMode.value(ifLight: String, ifDark: String) = if (this == ColorMode.Dark) ifDark else ifLight
enum class Direction { LTR, RTL }

class ConfigScope {
    var useSystemColorMode: Boolean = false
    var initialColorMode: ColorMode = ColorMode.Light
}

class ComponentListScope {
    internal val jsObject: Record<String, Any> = recordOf()
    infix fun String.toSinglePartComponent(block: SinglePartComponentScope.() -> Unit) { jsObject[this] = SinglePartComponentScope().apply(block).jsObject }
    fun String.toMultiPartComponent(parts: List<String>, block: MultiPartComponentScope.() -> Unit) {
        jsObject[this] = MultiPartComponentScope(parts).apply(block).jsObject
    }
}

class SinglePartComponentScope {
    internal val jsObject: Record<String, Any> = recordOf()
    fun baseStyle(block: StyleScope.(StyleProps) -> Unit) = jsObject.reactiveStyleBlock("baseStyle", block)
    fun sizes(block: ObjectScope.() -> Unit) = jsObject.objectBlock("sizes", block)
    fun variants(block: ObjectScope.() -> Unit) = jsObject.objectBlock("variants", block)
    fun defaultProps(block: ComponentDefaultScope.() -> Unit) {
        val componentDefaultScope = ComponentDefaultScope().apply(block)
        jsObject["defaultProps"] = mapOf("size" to componentDefaultScope.size, "variant" to componentDefaultScope.variant).filter { it.value != null }.asJsObject()
    }
}

class MultiPartComponentScope(parts: List<String>) {
    internal val jsObject: Record<String, Any> = recordOf("parts" to parts.toTypedArray())
    private fun <V: Any> Record<String, V>.multiPartObjectBlock(label: String, block: MultiPartObjectScope.() -> Unit) { this[label] = MultiPartObjectScope().apply(block).jsObject }
    fun baseStyle(block: ObjectScope.() -> Unit) = jsObject.objectBlock("baseStyle", block)
    fun sizes(block: MultiPartObjectScope.() -> Unit) = jsObject.multiPartObjectBlock("sizes", block)
    fun variants(block: MultiPartObjectScope.() -> Unit) = jsObject.multiPartObjectBlock("variants", block)
    fun defaultProps(block: ComponentDefaultScope.() -> Unit) {
        val componentDefaultScope = ComponentDefaultScope().apply(block)
        jsObject["defaultProps"] = mapOf("size" to componentDefaultScope.size, "variant" to componentDefaultScope.variant).filter { it.value != null }.asJsObject()
    }
}

class MultiPartObjectScope {
    internal val jsObject: Record<String, Any> = recordOf()
    infix fun String.toObject(block: ObjectScope.(StyleProps) -> Unit) = jsObject.reactiveObjectBlock(this, block)
}


class ComponentDefaultScope {
    var size: String? = null
    var variant: String? = null
}

class TransitionScope {
    internal val jsObject: Record<String, Any> = recordOf()
    private fun valueBlock(label: String, block: ValueScope.() -> Unit) { jsObject[label] = ValueScope().apply(block).jsObject }
    fun property(block: ValueScope.() -> Unit) = valueBlock("property", block)
    fun easing(block: ValueScope.() -> Unit) = valueBlock("easing", block)
    fun duration(block: ValueScope.() -> Unit) = valueBlock("duration", block)
}

class TypographyScope {
    internal val jsObject: Record<String, Any> = recordOf()
    private fun valueBlock(label: String, block: ValueScope.() -> Unit) { jsObject[label] = ValueScope().apply(block).jsObject }
    fun letterSpacings(block: ValueScope.() -> Unit) = valueBlock("letterSpacings", block)
    fun lineHeights(block: ValueScope.() -> Unit) = valueBlock("lineHeights", block)
    fun fontWeights(block: ValueScope.() -> Unit) = valueBlock("fontWeights", block)
    fun fonts(block: ValueScope.() -> Unit) = valueBlock("fonts", block)
    fun fontSizes(block: ValueScope.() -> Unit) = valueBlock("fontSizes", block)
}

class LayerScope {
    internal val jsObject: Record<String, Any> = recordOf()
    infix fun String.toObject(block: LayerStyleScope.() -> Unit) = jsObject.layerStyleBlock(this, block)
}

class LayerStyleScope : StyleScope() {
    fun String.toReactive(ifLight: String, ifDark: String) {
        jsObject[this] = ifLight
        jsObject[".chakra-ui-dark &"] = (jsObject[".chakra-ui-dark &"] as? Record<String, Any> ?: recordOf()) + recordOf(Pair(this, ifDark))
    }
}

private fun <V: Any> Record<String, V>.styleBlock(label: String, block: StyleScope.() -> Unit) { this[label] = StyleScope().apply(block).jsObject }
private fun <V: Any> Record<String, V>.reactiveStyleBlock(label: String, block: StyleScope.(StyleProps) -> Unit) {
    this[label] = { props: GlobalStyleProps ->
        StyleScope().apply{ block.invoke(this, props.toStyleProps()) }.jsObject
    }
}
private fun <V: Any> Record<String, V>.valueBlock(label: String, block: ValueScope.() -> Unit) { this[label] = ValueScope().apply(block).jsObject }
private fun <V: Any> Record<String, V>.objectBlock(label: String, block: ObjectScope.() -> Unit) { this[label] = ObjectScope().apply(block).jsObject }
private fun <V: Any> Record<String, V>.reactiveObjectBlock(label: String, block: ObjectScope.(StyleProps) -> Unit) { this[label] = { props: GlobalStyleProps -> ObjectScope().apply{ block.invoke(this, props.toStyleProps()) }.jsObject } }
private fun <V: Any> Record<String, V>.layerBlock(label: String, block: LayerScope.() -> Unit) { this[label] = LayerScope().apply(block).jsObject }
private fun <V: Any> Record<String, V>.layerStyleBlock(label: String, block: LayerStyleScope.() -> Unit) { this[label] = LayerStyleScope().apply(block).jsObject }
