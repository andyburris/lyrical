package ui.setup

import ui.khakra.Heading1
import Screen
import SetupAction
import Spacing
import VerticalSpacing
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.hooks.useDisclosure
import com.github.mpetuska.khakra.image.Image
import com.github.mpetuska.khakra.kt.set
import com.github.mpetuska.khakra.layout.Box
import com.github.mpetuska.khakra.layout.Container
import com.github.mpetuska.khakra.layout.Stack
import com.github.mpetuska.khakra.layout.VStack
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
    Box({ backgroundColor = "brand.background" }) {
        Container({maxW = "1280px"; p = arrayOf("32", "48", "64")}) {
            Stack({spacing= arrayOf("32", "48", "64")}) {
                AppHeader(props.state.selectedPlaylists.isNotEmpty() && props.state.config.amountOfSongs > 0) {
                    props.onUpdateSetup.invoke(SetupAction.StartGame(props.state.selectedPlaylists, props.state.config))
                }
                Stack({direction= arrayOf("column", "column", "row"); spacing= arrayOf("32", "48", "64")}) {
                    Sidebar(props.state, props.onUpdateSetup)
                    FindPlaylists(
                        props.state.addPlaylistState,
                        onAction = { props.onUpdateSetup.invoke(it) }
                    )
                }
            }
        }
    }
/*    Screen {
        css {
            gap = Gap("64px")
            justifyContent = JustifyContent.start
        }
    }*/
}


private fun RBuilder.AppHeader(canStartGame: Boolean, onPlayGameClick: () -> Unit) {
    flexbox(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        css { width = 100.pct }
        flexbox(alignItems = Align.center, gap = 32.px) {
            Image({
                src = "/assets/LyricalIcon.svg"
                boxSize = arrayOf("40", "48", "64")
            })
            Heading1 { +"Lyrical" }
        }
        Button({
            isDisabled = !canStartGame
            onClick = {
                onPlayGameClick.invoke()
            }
            size = "fab"
        }) {
            +"Play Game".toUpperCase()
        }
    }
}

private fun RBuilder.Sidebar(setupState: State.Setup, onUpdateSetup: (SetupAction) -> Unit) {
    VStack({spacing = arrayOf("8", "16", "32"); w = arrayOf("100%", "100%", "30%"); minWidth = arrayOf("0px", "350px", "400px")}) {
        VStack({
            layerStyle="primaryCard"
            width="100%"
            alignItems="stretch"
        }) {
            val disclosure = useDisclosure()
            SectionHeader("${setupState.selectedPlaylists.size} Playlists Selected", disclosure.isOpen) { disclosure.onToggle() }
            Collapse({
                `in` = disclosure.isOpen
                this["marginTop"] = "0px"
            }) {
                VerticalSpacing("24", "32")
                VStack({
                    w="100%"
                    spacing="16"
                }) {
                    setupState.selectedPlaylists.forEach {
                        HorizontalPlaylistItem(it, true) {
                            onUpdateSetup.invoke(SetupAction.RemovePlaylist(it))
                        }
                    }
                }
            }
        }
        VStack({
            layerStyle="card"
            width="100%"
            alignItems="stretch"
        }) {
            val disclosure = useDisclosure()
            SectionHeader("Options", disclosure.isOpen) { disclosure.onToggle() }
            Collapse({
                `in` = disclosure.isOpen
                this["mt"] = "0"
            }) {
                VerticalSpacing("24", "32")
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
            transition = "transform 200ms"
            transform = if (open) "rotate(90deg)" else "rotate(0deg)"
/*            transition("transform", duration = 200.ms)
            transform {
                rotate(if (open) 90.deg else 0.deg)
            }*/
        }
        attrs {
            onClickFunction = {
                onToggle.invoke()
            }
        }
    }
}