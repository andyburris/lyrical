package ui.setup

import PlaylistSearchState
import authenticateUser
import com.adamratzman.spotify.models.SimplePlaylist
import flexbox
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.TextDecorationLine
import kotlinx.html.js.*
import logout
import org.w3c.dom.HTMLElement
import react.*
import react.dom.*
import styled.*
import targetInputValue
import ui.common.Icon
import ui.theme
import useOnOutsideClick

fun RBuilder.FindPlaylists(addPlaylistState: State.Setup.AddPlaylistState, onUpdateSearch: (term: String) -> Unit, onAddPlaylist: (SimplePlaylist) -> Unit) = child(findPlaylists) {
    attrs {
        this.addPlaylistState = addPlaylistState
        this.onUpdateSearch = onUpdateSearch
        this.onAddPlaylist = onAddPlaylist
    }
}

external interface FindPlaylistsProps : RProps {
    var addPlaylistState: State.Setup.AddPlaylistState
    var onUpdateSearch: (term: String) -> Unit
    var onAddPlaylist: (SimplePlaylist) -> Unit
}

val findPlaylists = functionalComponent<FindPlaylistsProps> { props ->
    flexbox(direction = FlexDirection.column, gap = 32.px) {
        h2 { +"Add Playlists" }

        val (selected, setSelected) = useState(false)
        val ref = useRef<HTMLElement?>(null)
        flexbox(direction = FlexDirection.column, gap = 48.px) {
            css {
                backgroundColor = theme.overlay
                borderRadius = if (selected) 16.px else 32.px
                padding(horizontal = if (selected) 48.px else 24.px, vertical = if (selected) 48.px else 18.px)
            }
            SearchBar("Search by name or URL", 100.pct, onFocus = { setSelected(true) }) { props.onUpdateSearch.invoke(it) }
            if (selected) {
                flexbox(direction = FlexDirection.column, gap = 32.px) {
                    p { +"SPOTIFY PLAYLISTS" }
                    PlaylistSearchResults(
                        searchState = props.addPlaylistState.spotifySearchState,
                        onAddPlaylist = props.onAddPlaylist,
                        onLoginWithSpotify = { authenticateUser() }
                    )
                }

                flexbox(direction = FlexDirection.column, gap = 32.px) {
                    p {
                        +"MY PLAYLISTS "
                        if (props.addPlaylistState.myPlaylistSearchState !is PlaylistSearchState.RequiresLogin) {
                            styledSpan {
                                css {
                                    color = theme.onBackgroundPlaceholder
                                    hover {
                                        textDecoration = TextDecoration(lines = setOf(TextDecorationLine.underline))
                                    }
                                }
                                +"(Logout)"
                                attrs.onClickFunction = {
                                    logout()
                                }
                            }
                        }
                    }
                    PlaylistSearchResults(
                        searchState = props.addPlaylistState.myPlaylistSearchState,
                        onAddPlaylist = props.onAddPlaylist,
                        onLoginWithSpotify = { authenticateUser() }
                    )
                }
            }
            attrs["ref"] = ref
            useOnOutsideClick(ref) {
                println("clicked outside")
                if (selected) setSelected(false)
            }
        }
    }
}

private fun RBuilder.SearchBar(placeholder: String, maxWidth: LinearDimension, onFocus: () -> Unit, onTermUpdate: (String) -> Unit) {
    flexbox(alignItems = Align.center, justifyContent = JustifyContent.start) {
        css {
            this.maxWidth = maxWidth
        }
        Icon(Icon.Search)
        styledInput {
            css {
                padding(horizontal = 16.px)
                fontFamily = "Inter"
                fontSize = 24.px
                fontWeight = FontWeight.w700
                border = "none"
                width = 100.pct
                backgroundColor = Color.transparent
                color = theme.onBackground
            }

            attrs {
                this.placeholder = placeholder
                onChangeFunction = {
                    val value = it.targetInputValue
                    onTermUpdate(value)
                }
                this.onFocusFunction = { println("focused"); onFocus.invoke() }
            }
        }
    }
}

private fun RBuilder.PlaylistSearchResults(searchState: PlaylistSearchState, onAddPlaylist: (SimplePlaylist) -> Unit, onLoginWithSpotify: () -> Unit = {}) {
    when(searchState) {
        is PlaylistSearchState.Results -> {
            flexbox(justifyContent = JustifyContent.start, gap = 32.px, wrap = FlexWrap.wrap) {
                searchState.playlists.forEach { (playlist, checked) ->
                    div {
                        PlaylistItem(playlist, checked) {
                            onAddPlaylist.invoke(playlist)
                        }
                    }
                }
            }
        }
        PlaylistSearchState.RequiresLogin -> {
            flexbox(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
                css {
                    backgroundColor = theme.overlay
                    borderRadius = 16.px
                    padding(48.px)
                }
                flexbox(justifyContent = JustifyContent.start, alignItems = Align.center, gap = 16.px) {
                    Icon(Icon.Login)
                    styledP {
                        css { color = theme.onBackground }
                        +"Login to see your playlists"
                    }
                }
                button {
                    +"LOGIN WITH SPOTIFY"
                    attrs.onClickFunction = {
                        onLoginWithSpotify.invoke()
                    }
                }
            }
        }
        PlaylistSearchState.Loading -> {
            flexbox(justifyContent = JustifyContent.start, gap = 32.px, wrap = FlexWrap.wrap) {
                repeat(20) {
                    LoadingPlaylistItem()
                }
            }
        }
        PlaylistSearchState.Error -> {
            h1 { +"Error" }
        }
    }
}