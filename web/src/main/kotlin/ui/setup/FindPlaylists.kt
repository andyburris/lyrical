package ui.setup

import PlaylistSearchState
import SetupAction
import flexColumn
import flexRow
import flexbox
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.TextDecorationLine
import kotlinx.html.js.*
import logout
import react.*
import react.dom.*
import styled.*
import targetInputValue
import ui.common.Icon
import ui.theme

fun RBuilder.FindPlaylists(addPlaylistState: State.Setup.AddPlaylistState, onAction: (SetupAction) -> Unit) = child(findPlaylists) {
    attrs {
        this.addPlaylistState = addPlaylistState
        this.onAction = onAction
    }
}

external interface FindPlaylistsProps : RProps {
    var addPlaylistState: State.Setup.AddPlaylistState
    var onAction: (SetupAction) -> Unit
}

val findPlaylists = functionalComponent<FindPlaylistsProps> { props ->
    flexColumn(gap = 48.px) {
        SearchBar("Search by name or URL") { props.onAction.invoke(SetupAction.UpdateSearch(it)) }
        MyPlaylists(props)
        SpotifyPlaylists(props)
    }
}

private fun RBuilder.SpotifyPlaylists(props: FindPlaylistsProps) {
    flexbox(direction = FlexDirection.column, gap = 32.px) {
        css { width = 100.pct }
        p { +"SPOTIFY PLAYLISTS" }
        PlaylistSearchResults(
            searchState = props.addPlaylistState.spotifySearchState,
            onTogglePlaylist = props.onAction,
        )
    }
}

private fun RBuilder.MyPlaylists(props: FindPlaylistsProps) {
    flexbox(direction = FlexDirection.column, gap = 32.px) {
        css { width = 100.pct }
        p {
            +"MY PLAYLISTS "
        }
        PlaylistSearchResults(
            searchState = props.addPlaylistState.myPlaylistSearchState,
            onTogglePlaylist = props.onAction,
        )
    }
}


private fun RBuilder.LogoutSpan() {
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

private fun RBuilder.SearchBar(placeholder: String, onTermUpdate: (String) -> Unit) {
    flexbox(alignItems = Align.center, justifyContent = JustifyContent.start) {
        css {
            width = 100.pct
            padding(horizontal = 24.px, vertical = 16.px)
            backgroundColor = theme.backgroundCard
            borderRadius = 32.px
            boxSizing = BoxSizing.borderBox
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
                borderRadius = 32.px
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

private fun RBuilder.PlaylistSearchResults(searchState: PlaylistSearchState, onTogglePlaylist: (SetupAction) -> Unit) {
    when(searchState) {
        is PlaylistSearchState.Results -> {
            flexbox(justifyContent = JustifyContent.start, gap = 16.px, wrap = FlexWrap.wrap) {
                searchState.playlists.forEach { (playlist, checked) ->
                    div {
                        VerticalPlaylistItem(playlist, checked) {
                            val action = when(checked) {
                                true -> SetupAction.RemovePlaylist(playlist)
                                false -> SetupAction.AddPlaylist(playlist)
                            }
                            onTogglePlaylist.invoke(action)
                        }
                    }
                }
            }
        }
        PlaylistSearchState.Loading -> {
            flexRow(justifyContent = JustifyContent.start, gap = 32.px, wrap = FlexWrap.wrap) {
                repeat(20) {
                    LoadingVerticalPlaylistItem()
                }
            }
        }
        PlaylistSearchState.Error -> {
            h1 { +"Error" }
        }
    }
}