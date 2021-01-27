package ui.setup

import Screen
import SetupAction
import flexbox
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import styled.*
import ui.common.Icon
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
            props.onUpdateSetup.invoke(SetupAction.StartGame(props.state.selectedPlaylists.map { it.uri.uri }, props.state.config))
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
                            PlaylistItem(it, true) {
                                props.onUpdateSetup.invoke(SetupAction.RemovePlaylist(it))
                            }
                        }
                    }
                }
            }

            val (optionsOpen, setOptionsOpen) = useState(false)
            SectionHeader(Icon.List, "Options", optionsOpen) { setOptionsOpen(!optionsOpen) }
            if (optionsOpen) {
                Options(props.state.config) {
                    props.onUpdateSetup.invoke(SetupAction.UpdateConfig(it))
                }
            }
        }

        FindPlaylists(
            props.state.addPlaylistState,
            onUpdateSearch = {
                props.onUpdateSetup.invoke(SetupAction.UpdateSearch(it))
            },
            onAddPlaylist = {
                props.onUpdateSetup.invoke(SetupAction.AddPlaylist(it))
            }
        )
    }
}

private fun RBuilder.AppHeader(canStartGame: Boolean, onPlayGameClick: () -> Unit) {
    flexbox(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        flexbox(alignItems = Align.center, gap = 32.px) {
            styledImg(src = "/assets/LyricalIcon.svg") {
                css { width = 72.px; height = 72.px }
            }
            styledH1 { +"Lyrical" }
        }
        styledButton {
            css {
                backgroundColor = theme.primary.withAlpha(if (canStartGame) 1.0 else 0.25)
                cursor = if (canStartGame) Cursor.pointer else Cursor.default
                color = if (canStartGame) theme.onBackground else theme.onBackgroundSecondary
            }
            attrs {
                disabled = !canStartGame
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