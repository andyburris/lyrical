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
import react.functionalComponent
import styled.css
import styled.styledDiv
import styled.styledInput
import styled.styledP
import targetInputValue
import ui.common.Icon
import ui.theme

fun RBuilder.FindPlaylists(tab: State.Setup.TabState, onUpdateTab: (tab: Screen.Setup.Tab) -> Unit, onAddPlaylist: (SimplePlaylist) -> Unit) = child(findPlaylists) {
    attrs {
        this.tab = tab
        this.onAddPlaylist = onAddPlaylist
        this.onUpdateTab = onUpdateTab
    }
}

external interface FindPlaylistsProps : RProps {
    var tab: State.Setup.TabState
    var onAddPlaylist: (SimplePlaylist) -> Unit
    var onUpdateTab: (tab: Screen.Setup.Tab) -> Unit
}

val findPlaylists = functionalComponent<FindPlaylistsProps> { props ->
    flexbox(direction = FlexDirection.column, gap = 32.px) {
        flexbox(justifyContent = JustifyContent.start, alignItems = Align.start, gap = 32.px) {
            Tab("Spotify Playlists", props.tab is State.Setup.TabState.SpotifyPlaylists) {
                if (props.tab !is State.Setup.TabState.SpotifyPlaylists) {
                    println("switching to SpotifyPlaylists tab")
                    props.onUpdateTab.invoke(Screen.Setup.Tab.SpotifyPlaylists("") )
                }
            }
            Tab("My Playlists", props.tab is State.Setup.TabState.MyPlaylists) {
                if (props.tab !is State.Setup.TabState.MyPlaylists) {
                    println("switching to MyPlaylists tab")
                    props.onUpdateTab.invoke(Screen.Setup.Tab.MyPlaylists("") )
                }
            }
            Tab("Add By URL", props.tab is State.Setup.TabState.URL) {
                if (props.tab !is State.Setup.TabState.URL) {
                    println("switching to URL tab")
                    props.onUpdateTab.invoke(Screen.Setup.Tab.URL("") )
                }
            }
        }
        when(val tab = props.tab) {
            is State.Setup.TabState.SpotifyPlaylists -> {
                SearchBar("Search playlists", 512.px) { props.onUpdateTab.invoke(tab.toTab().copy(searchTerm = it)) }
                PlaylistSearchResults(tab.searchState, props.onAddPlaylist)
            }
            is State.Setup.TabState.MyPlaylists -> {
                MyPlaylists(tab, props.onAddPlaylist) { props.onUpdateTab.invoke(tab.toTab().copy(searchTerm = it)) }
            }
            is State.Setup.TabState.URL -> {
                AddByURL(tab, props.onAddPlaylist) { props.onUpdateTab.invoke(tab.toTab().copy(searchURL = it)) }
            }
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

private fun RBuilder.MyPlaylists(tab: State.Setup.TabState.MyPlaylists, onAddPlaylist: (SimplePlaylist) -> Unit, onTermUpdate: (String) -> Unit) {
    SearchBar("Filter playlists", 512.px, onTermUpdate)
    PlaylistSearchResults(tab.searchState, onAddPlaylist) {
        authenticateUser()
    }
}

private fun RBuilder.AddByURL(tab: State.Setup.TabState.URL, onAddPlaylist: (SimplePlaylist) -> Unit, onURLUpdate: (String) -> Unit) {
    SearchBar("Playlist URL", 100.pct, onURLUpdate)
    PlaylistSearchResults(tab.searchState, onAddPlaylist)
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