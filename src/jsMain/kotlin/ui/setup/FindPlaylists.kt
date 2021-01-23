package ui.setup

import PlaylistSearchState
import authenticateUser
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimplePlaylist
import flexbox
import kotlinx.css.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.child
import react.dom.button
import react.dom.h1
import react.dom.h2
import react.dom.p
import react.functionalComponent
import styled.css
import styled.styledDiv
import styled.styledInput
import styled.styledP
import targetInputValue
import ui.common.Icon
import ui.theme

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
    flexbox(direction = FlexDirection.column, gap = 64.px) {
/*        styledP {
            css { color = theme.onBackground }
            +"ADD PLAYLISTS"
        }*/
        flexbox(direction = FlexDirection.column, gap = 32.px) {
            h2 { +"Add Playlists" }
            SearchBar("Search by name or URL", 100.pct) { props.onUpdateSearch.invoke(it) }
        }

        flexbox(direction = FlexDirection.column, gap = 32.px) {
            p { +"SPOTIFY PLAYLISTS" }
            PlaylistSearchResults(props.addPlaylistState.spotifySearchState, props.onAddPlaylist)
        }

        flexbox(direction = FlexDirection.column, gap = 32.px) {
            p { +"MY PLAYLISTS" }
            PlaylistSearchResults(props.addPlaylistState.myPlaylistSearchState, props.onAddPlaylist)
        }
    }
}

private fun RBuilder.Tab(title: String, selected: Boolean, onClick: () -> Unit) {
    flexbox(direction = FlexDirection.column, gap = 8.px, alignItems = Align.stretch) {
        css { cursor = Cursor.pointer }
        styledP {
            css {
                color = if (selected) theme.onBackground else theme.onBackgroundSecondary
            }
            +title.toUpperCase()
        }
        if (selected) {
            styledDiv {
                css {
                    height = 4.px
                    backgroundColor = theme.primary
                }
            }
        }
        attrs {
            onClickFunction = {
                onClick.invoke()
            }
        }
    }
}

private fun RBuilder.SearchBar(placeholder: String, maxWidth: LinearDimension, onTermUpdate: (String) -> Unit) {
    flexbox(alignItems = Align.center, justifyContent = JustifyContent.start) {
        css {
            this.maxWidth = maxWidth
            backgroundColor = theme.onBackground
            borderRadius = 32.px
            padding(horizontal = 16.px)
        }
        Icon(Icon.Search, colorFilter = "brightness(0) saturate(100%) invert(10%) sepia(58%) saturate(0%) hue-rotate(230deg) brightness(98%) contrast(77%)")
        styledInput {
            css {
                padding(vertical = 18.px, horizontal = 16.px)
                fontFamily = "Inter"
                fontSize = 24.px
                fontWeight = FontWeight.w700
                border = "none"
                width = 100.pct
                backgroundColor = Color.transparent
            }

            attrs {
                this.placeholder = placeholder
                onChangeFunction = {
                    val value = it.targetInputValue
                    onTermUpdate(value)
                }
            }
        }
    }
}

private fun RBuilder.PlaylistSearchResults(searchState: PlaylistSearchState, onAddPlaylist: (SimplePlaylist) -> Unit, onLoginWithSpotify: () -> Unit = {}) {
    when(searchState) {
        is PlaylistSearchState.Results -> {
            flexbox(justifyContent = JustifyContent.start, gap = 32.px, wrap = FlexWrap.wrap) {
                searchState.playlists.forEach {
                    PlaylistItem(it.first, it.second) { onAddPlaylist.invoke(it.first) }
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