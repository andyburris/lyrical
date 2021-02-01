package ui.setup

import GameConfig
import flexColumn
import flexRow
import flexbox
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import onTextChanged
import react.*
import size
import styled.css
import styled.styledDiv
import styled.styledInput
import styled.styledP
import ui.common.Chip
import ui.theme
import kotlin.time.ExperimentalTime

fun RBuilder.Options(options: GameConfig, onUpdate: (GameConfig) -> Unit) = child(optionsComponent) {
    attrs {
        this.options = options
        this.onUpdate = onUpdate
    }
}
external interface OptionsProps : RProps {
    var options: GameConfig
    var onUpdate: (GameConfig) -> Unit
}
@OptIn(ExperimentalTime::class)
val optionsComponent = functionalComponent<OptionsProps> { props ->
    flexColumn(gap = 16.px) {
        css { width = 100.pct }
        NumberPickerItem("Number of songs", 10, props.options.amountOfSongs) { props.onUpdate.invoke(props.options.copy(amountOfSongs = it)) }
        //NumberPickerItem("Timer", 0, props.options.timer) { props.onUpdate.invoke(props.options.copy(timer = it)) }
        SwitchItem("Show source playlist", props.options.showSourcePlaylist) { props.onUpdate.invoke(props.options.copy(showSourcePlaylist = it)) }
        SwitchItem("Split playlists evenly", props.options.distributePlaylistsEvenly) { props.onUpdate.invoke(props.options.copy(distributePlaylistsEvenly = it)) }
        ChipItem("Difficulty", listOf(Difficulty.Easy, Difficulty.Medium, Difficulty.Hard), stringify = { it.name.toUpperCase() }, selected = props.options.difficulty) { props.onUpdate.invoke(props.options.copy(difficulty = it)) }
    }
}

private fun RBuilder.PickerItem(label: String, content: RBuilder.() -> Unit) {
    flexbox(justifyContent = JustifyContent.spaceBetween) {
        css { width = 100.pct }
        styledP {
            css {
                color = theme.onBackground
                fontSize = 18.px
            }
            +label
        }
        content()
    }
}

private fun RBuilder.NumberPickerItem(label: String, default: Int, current: Int, onChange: (Int) -> Unit) {
    val (currentText, setCurrentText) = useState(if (current == default) "" else current.toString())
    PickerItem(label) {
        styledInput {
            css {
                border = "none"
                borderRadius = 4.px
                width = 48.px
                height = 30.px
                backgroundColor = theme.backgroundCard
                textAlign = TextAlign.center
                fontFamily = "Inter"
                fontWeight = FontWeight.w700
                fontSize = 18.px
                color = theme.onBackground
            }
            attrs {
                value = currentText
                placeholder = default.toString()
                onTextChanged { text ->
                    val filtered = text.filter { it in '0'..'9' }
                    setCurrentText(filtered)
                    onChange.invoke(filtered.toIntOrNull() ?: default)
                }
            }
        }
    }
}

private fun RBuilder.SwitchItem(label: String, current: Boolean, onChange: (Boolean) -> Unit) {
    PickerItem(label) {
        flexbox(justifyContent = if (current) JustifyContent.flexEnd else JustifyContent.start, alignItems = Align.center) {
            css {
                width = 40.px
                padding(all = 4.px)
                backgroundColor = if (current) theme.primary else theme.backgroundCard
                borderRadius = 20.px
            }
            styledDiv {
                css {
                    size(20.px)
                    backgroundColor = if (current) theme.onPrimary else theme.onBackgroundPlaceholder
                    borderRadius = 16.px
                }
            }
            attrs {
                onClickFunction = {
                    onChange.invoke(!current)
                }
            }
        }
    }
}

private fun <T> RBuilder.ChipItem(label: String, items: List<T>, selected: T, stringify: (T) -> String = { it.toString() }, onChange: (T) -> Unit) {
    PickerItem(label) {
        flexRow(gap = 4.px) {
            items.forEach {
                Chip(stringify(it), backgroundColor = if (it == selected) theme.primary else theme.backgroundCard, textColor = if (it == selected) theme.onPrimary else theme.onBackgroundSecondary, fontSize = 12.px) { onChange.invoke(it) }
            }
        }
    }
}