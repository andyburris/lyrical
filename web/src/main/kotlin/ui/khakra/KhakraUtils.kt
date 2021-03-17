package ui.khakra

import com.github.mpetuska.khakra.KhakraDSL
import com.github.mpetuska.khakra.checkbox.CheckboxProps
import com.github.mpetuska.khakra.input.InputProps
import com.github.mpetuska.khakra.kt.Builder
import com.github.mpetuska.khakra.kt.get
import com.github.mpetuska.khakra.kt.set
import com.github.mpetuska.khakra.layout.Heading
import com.github.mpetuska.khakra.layout.HeadingProps
import com.github.mpetuska.khakra.layout.Text
import com.github.mpetuska.khakra.layout.TextProps
import com.github.mpetuska.khakra.switch.SwitchProps
import com.github.mpetuska.khakra.system.ChakraProps
import com.github.mpetuska.khakra.theme.ChakraTheme
import kotlinext.js.Record
import org.w3c.dom.events.Event
import react.RBuilder
import react.RElementBuilder
import react.RProps
import react.ReactElement

var ChakraProps.onClick: () -> Unit
    get() = this["onClick"]
    set(value) { this["onClick"] = value }
var InputProps.onChange: (Event) -> Unit
    get() = this["onChange"]
    set(value) { this["onChange"] = value }
var InputProps.onKeyUp: (Event) -> Unit
    get() = this["onKeyUp"]
    set(value) { this["onKeyUp"] = value }
var SwitchProps.isChecked: Boolean
    get() = this["isChecked"]
    set(value) { this["isChecked"] = value }

fun extendTheme(overrides: ChakraThemeConfig): Record<String, Any> = com.github.mpetuska.khakra.react.extendTheme<ChakraTheme>(overrides.toRecord())
fun extendTheme(overrides: Record<String, Any>): Record<String, Any> = com.github.mpetuska.khakra.react.extendTheme<ChakraTheme>(overrides)