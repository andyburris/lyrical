package ui.setup

import GameConfig
import flexColumn
import flexbox
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import onTextChanged
import react.*
import react.dom.p
import size
import styled.css
import styled.styledDiv
import styled.styledInput
import styled.styledP
import ui.theme
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

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
        NumberPickerItem("Timer", 0, props.options.timer.inSeconds.toInt()) { props.onUpdate.invoke(props.options.copy(timer = it.seconds)) }
        SwitchItem("Show source playlist", props.options.showSourcePlaylist) { props.onUpdate.invoke(props.options.copy(showSourcePlaylist = it)) }
        SwitchItem("Split playlists evenly", props.options.distributePlaylistsEvenly) { props.onUpdate.invoke(props.options.copy(distributePlaylistsEvenly = it)) }
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
                backgroundColor = theme.background
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
        flexbox(justifyContent = if (current) JustifyContent.end else JustifyContent.start, alignItems = Align.center) {
            css {
                width = 40.px
                padding(all = 4.px)
                backgroundColor = theme.onBackgroundPlaceholder
                borderRadius = 20.px
            }
            styledDiv {
                css {
                    size(20.px)
                    backgroundColor = if (current) theme.primary else theme.onBackgroundSecondary
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