package ui.setup

import ui.khakra.Heading1
import Screen
import SetupAction
import Spacing
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.hooks.useDisclosure
import com.github.mpetuska.khakra.image.Image
import com.github.mpetuska.khakra.layout.Box
import com.github.mpetuska.khakra.layout.Container
import com.github.mpetuska.khakra.layout.Stack
import com.github.mpetuska.khakra.transition.Collapse
import flexColumn
import flexbox
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.onClickFunction
import onHorizontalLayout
import onVerticalLayout
import react.*
import styled.*
import ui.common.Icon
import ui.khakra.SectionHeader
import ui.khakra.Subtitle1
import ui.khakra.onClick
import ui.theme

fun RBuilder.SetupScreen(state: State.Setup, onUpdateSetup: (SetupAction) -> Unit) = child(setup) {
    attrs {
        this.state = state
        this.onUpdateSetup = onUpdateSetup
    }
}
external interface SetupProps : RProps {
    var state: State.Setup
    var onUpdateSetup: (SetupAction) -> Unit
}
val setup = functionalComponent<SetupProps> { props ->

    Screen {
        css {
            gap = Gap("64px")
            justifyContent = JustifyContent.start
        }
        AppHeader(props.state.selectedPlaylists.isNotEmpty() && props.state.config.amountOfSongs > 0) {
            props.onUpdateSetup.invoke(SetupAction.StartGame(props.state.selectedPlaylists, props.state.config))
        }
        flexbox(gap = 64.px) {
            css {
                onVerticalLayout {
                    flexDirection = FlexDirection.column
                }
            }
            Sidebar(props.state, props.onUpdateSetup)
            FindPlaylists(
                props.state.addPlaylistState,
                onAction = { props.onUpdateSetup.invoke(it) }
            )
        }
    }
}


private fun RBuilder.AppHeader(canStartGame: Boolean, onPlayGameClick: () -> Unit) {
    flexbox(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        css { width = 100.pct }
        flexbox(alignItems = Align.center, gap = 32.px) {
            Image({
                src = "/assets/LyricalIcon.svg"
                boxSize = "20"
            })
            Heading1 { +"Lyrical" }
        }
        Button({
            isDisabled = !canStartGame
            onClick = {
                onPlayGameClick.invoke()
            }
            size = "lg"
        }) {
            +"Play Game".toUpperCase()
        }
    }
}

private fun RBuilder.Sidebar(setupState: State.Setup, onUpdateSetup: (SetupAction) -> Unit) {
    flexbox(FlexDirection.column, gap = 32.px) {
        css {
            onHorizontalLayout {
                width = 30.pct
                minWidth = 400.px
                position = Position.sticky
                alignSelf = Align.flexStart
                top = 0.px
                marginTop = (-64).px
                paddingTop = 64.px
            }
            onVerticalLayout {
                width = 100.pct
            }
        }
        flexColumn(gap = 32.px, alignItems = Align.stretch) {
            css {
                padding(32.px)
                borderRadius = 16.px
                backgroundColor = theme.primary
                width = 100.pct
                boxSizing = BoxSizing.borderBox
                overflow = Overflow.hidden
            }
            val disclosure = useDisclosure()
            SectionHeader("${setupState.selectedPlaylists.size} Playlists Selected", disclosure.isOpen) { disclosure.onToggle() }
            Collapse({`in` = disclosure.isOpen}) {
                flexColumn(gap = 16.px) {
                    css { width = 100.pct }
                    setupState.selectedPlaylists.forEach {
                        HorizontalPlaylistItem(it, true) {
                            onUpdateSetup.invoke(SetupAction.RemovePlaylist(it))
                        }
                    }
                }
            }
        }

        flexColumn(alignItems = Align.stretch) {
            css {
                padding(all = 32.px)
                backgroundColor = theme.backgroundDark
                borderRadius = 16.px
                width = 100.pct
                boxSizing = BoxSizing.borderBox
            }
            val disclosure = useDisclosure()
            SectionHeader("Options", disclosure.isOpen) { disclosure.onToggle() }
            Collapse({`in` = disclosure.isOpen}) {
                Spacing(verticalSpace = 32.px)
                Options(setupState.config) {
                    onUpdateSetup.invoke(SetupAction.UpdateConfig(it))
                }
            }
        }
    }
}

private fun RBuilder.SectionHeader(title: String, open: Boolean, icon: Icon? = null, onToggle: () -> Unit) {
    flexbox(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        css { width = 100.pct }
        flexbox(alignItems = Align.center, gap = 16.px) {
            icon?.let { Icon(it) }
            Subtitle1 {
                +title
            }
        }
        Icon(Icon.Arrow.Right) {
            transition("transform", duration = 200.ms)
            transform {
                rotate(if (open) 90.deg else 0.deg)
            }
        }
        attrs {
            onClickFunction = {
                onToggle.invoke()
            }
        }
    }
}