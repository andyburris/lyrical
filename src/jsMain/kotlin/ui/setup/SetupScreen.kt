package ui.setup

import GameConfig
import Screen
import Size
import com.adamratzman.spotify.models.SimplePlaylist
import flexbox
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import size
import styled.*
import ui.common.Icon
import ui.theme

fun RBuilder.SetupScreen(state: State.Setup, onUpdateSetup: (Screen.Setup) -> Unit, onPlayGame: (playlistURIs: List<String>, config: GameConfig) -> Unit) = child(setup) {
    attrs {
        this.state = state
        this.onUpdateSetup = onUpdateSetup
        this.onPlayGame = onPlayGame
    }
}
external interface SetupProps : RProps {
    var state: State.Setup
    var onPlayGame: (playlistURIs: List<String>, config: GameConfig) -> Unit
    var onUpdateSetup: (Screen.Setup) -> Unit
}
val setup = functionalComponent<SetupProps> { props ->
    Screen {
        css {
            gap = Gap("64px")
            justifyContent = JustifyContent.start
        }
        AppHeader {
            props.onPlayGame.invoke(props.state.selectedPlaylists.map { it.uri.uri }, props.state.config)
        }
        flexbox(direction = FlexDirection.column, gap = 48.px) {
            css {
                padding(48.px)
                backgroundColor = theme.overlay
                borderRadius = 16.px
                boxSizing = BoxSizing.borderBox
            }

            val (playlistsOpen, setPlaylistsOpen) = useState(false)
            flexbox(FlexDirection.column, gap = 32.px) {
                SectionHeader(Icon.Library, "${props.state.selectedPlaylists.size} Playlists Selected", playlistsOpen) { setPlaylistsOpen(!playlistsOpen) }
                if (playlistsOpen) {
                    flexbox(gap = 32.px, wrap = FlexWrap.wrap, justifyContent = JustifyContent.start) {
                        props.state.selectedPlaylists.forEach {
                            PlaylistItem(it, false) {
                                val screen = props.state.toScreen()
                                props.onUpdateSetup.invoke(screen.copy(selectedPlaylists = screen.selectedPlaylists - it))
                            }
                        }
                    }
                }
            }

            val (optionsOpen, setOptionsOpen) = useState(false)
            SectionHeader(Icon.List, "Options", optionsOpen) { setOptionsOpen(!optionsOpen) }
            if (optionsOpen) {
                flexbox(gap = 16.px) {

                }
            }
        }

        FindPlaylists(
            props.state.tabState,
            onUpdateTab = {
                val screen = props.state.toScreen()
                props.onUpdateSetup.invoke(screen.copy(tab = it))
            },
            onAddPlaylist = {
                val screen = props.state.toScreen()
                props.onUpdateSetup.invoke(screen.copy(selectedPlaylists = (screen.selectedPlaylists + it).distinct()))
            }
        )
    }
}

private fun RBuilder.AppHeader(onPlayGameClick: () -> Unit) {
    flexbox(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        flexbox(alignItems = Align.center, gap = 32.px) {
            styledImg(src = "/assets/LyricalIcon.svg") {
                css { width = 72.px; height = 72.px }
            }
            styledH1 { +"Lyrical" }
        }
        styledButton {
            attrs {
                onClickFunction = {
                    onPlayGameClick.invoke()
                }
            }
            +"Play Game".toUpperCase()
        }
    }
}

private fun RBuilder.SectionHeader(icon: Icon, title: String, open: Boolean, onToggle: () -> Unit) {
    flexbox(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        flexbox(alignItems = Align.center, gap = 16.px) {
            Icon(icon)
            styledP {
                css { color = theme.onBackground }
                +title
            }
        }
        Icon(if (open) Icon.Arrow.Down else Icon.Arrow.Right)
        attrs {
            onClickFunction = {
                onToggle.invoke()
            }
        }
    }
}