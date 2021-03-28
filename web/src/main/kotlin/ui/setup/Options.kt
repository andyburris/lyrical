package ui.setup

import GameConfig
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.colorMode.useColorMode
import com.github.mpetuska.khakra.input.Input
import com.github.mpetuska.khakra.kt.get
import com.github.mpetuska.khakra.kt.set
import com.github.mpetuska.khakra.layout.Box
import com.github.mpetuska.khakra.layout.HStack
import com.github.mpetuska.khakra.numberInput.UseNumberInputProps
import com.github.mpetuska.khakra.numberInput.useNumberInput
import com.github.mpetuska.khakra.switch.Switch
import com.github.mpetuska.khakra.transition.Fade
import flexColumn
import flexRow
import flexbox
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import onTextChanged
import react.*
import recordOf
import size
import styled.css
import styled.styledDiv
import styled.styledInput
import targetInputValue
import ui.ChakraTheme
import ui.common.Chip
import ui.common.Icon
import ui.khakra.*
import ui.theme
import kotlin.time.ExperimentalTime

fun RBuilder.GameOptions(options: GameConfig, onUpdate: (GameConfig) -> Unit) = child(optionsComponent) {
    attrs {
        this.options = options
        this.onUpdate = onUpdate
    }
}
external interface GameOptionsProps : RProps {
    var options: GameConfig
    var onUpdate: (GameConfig) -> Unit
}
@OptIn(ExperimentalTime::class)
val optionsComponent = functionalComponent<GameOptionsProps> { props ->
    flexColumn(gap = 16.px) {
        css { width = 100.pct }
        NumberPickerItem("Number of songs", 10, props.options.amountOfSongs, min = 1) { props.onUpdate.invoke(props.options.copy(amountOfSongs = it)) }
        //NumberPickerItem("Timer", 0, props.options.timer) { props.onUpdate.invoke(props.options.copy(timer = it)) }
        SwitchItem("Show source playlist", props.options.showSourcePlaylist) { props.onUpdate.invoke(props.options.copy(showSourcePlaylist = it)) }
        SwitchItem("Split playlists evenly", props.options.distributePlaylistsEvenly) { props.onUpdate.invoke(props.options.copy(distributePlaylistsEvenly = it)) }
        ChipItem("Difficulty", listOf(Difficulty.Easy, Difficulty.Medium, Difficulty.Hard), stringify = { it.name.toUpperCase() }, selected = props.options.difficulty) { props.onUpdate.invoke(props.options.copy(difficulty = it)) }
    }
}

private fun RBuilder.PickerItem(label: String, content: RBuilder.() -> Unit) {
    flexbox(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        css { width = 100.pct }
        Subtitle2 {
            +label
        }
        content()
    }
}

private fun RBuilder.NumberPickerItem(label: String, default: Int, current: Int, min: Int = 0, max: Int = Int.MAX_VALUE, onChange: (Int) -> Unit) {
    val (currentText, setCurrentText) = useState(if (current == default) "" else current.toString())
    PickerItem(label) {
        Input({
            size = "sm"
            variant = "filled"
            this["value"] = currentText
            this["placeholder"] = default.toString()
            bg = ChakraTheme.backgroundCard
            width = arrayOf(32, 40, 48)
            height = arrayOf(24, 28, 32)
            textAlign = "center"
            this.onChange = {
                val filtered = it.targetInputValue.filter { it in '0'..'9' }
                setCurrentText(filtered)
                onChange.invoke(filtered.toIntOrNull() ?: default)
            }
            this.onBlur = {
                if (current !in min..max) {
                    val inbounds = current.coerceIn(min..max)
                    setCurrentText(inbounds.toString())
                    onChange.invoke(inbounds)
                }
            }
        })
    }
}

private fun RBuilder.SwitchItem(label: String, current: Boolean, onChange: (Boolean) -> Unit) {
    PickerItem(label) {
        HStack({
            justifyContent = if (current) "flex-end" else "flex-start"
            backgroundColor = colorTheme() + if (current) "primary" else "backgroundCard"
            borderRadius = "full"
            width = arrayOf("32", "40", "48")
            p = 4
            `as` = "Button"
            outline = "none"
            transition = "all 0.2s cubic-bezier(.08,.52,.52,1)"
            this["_focus"] = recordOf(
                "boxShadow" to "outline"
            )
            onClick = {
                onChange.invoke(!current)
            }
        }) {
            Fade({
                `in` = true
                this["layout"] = true
            }) {
                Box({
                    boxSize = arrayOf("12", "16", "20")
                    backgroundColor = colorTheme() + if (current) "onPrimary" else "onBackgroundTernary"
                    borderRadius = "full"
                })
            }
        }
    }
}

private fun <T> RBuilder.ChipItem(label: String, items: List<T>, selected: T, stringify: (T) -> String = { it.toString() }, onChange: (T) -> Unit) {
    PickerItem(label) {
        flexRow(gap = 4.px) {
            items.forEach {
                Chip(stringify(it), props = {
                    if (it == selected) { variant = "solid" }
                    fontSize = arrayOf("0.5rem", "0.75rem")
                }) { onChange.invoke(it) }
            }
        }
    }
}

fun RBuilder.AppOptions(onLogout: () -> Unit) {
    flexColumn(gap = 16.px) {
        css { width = 100.pct }
        val darkMode = useColorMode()
        SwitchItem("Dark Mode", darkMode.colorMode == "dark") { darkMode.setColorMode?.invoke(if (it) "dark" else "light") }
        HStack({ `as` = "Button"; spacing = 16; onClick = onLogout }) {
            Icon(Icon.Login) { boxSize = "24" }
            Subtitle2 { +"Log out of Spotify" }
        }
    }
}