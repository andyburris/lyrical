package ui.setup

import PlaylistSearchState
import SetupAction
import com.github.mpetuska.khakra.clickable.useClickable
import com.github.mpetuska.khakra.icon.Icon
import com.github.mpetuska.khakra.input.Input
import com.github.mpetuska.khakra.input.InputGroup
import com.github.mpetuska.khakra.input.InputLeftElement
import com.github.mpetuska.khakra.kt.get
import com.github.mpetuska.khakra.kt.set
import com.github.mpetuska.khakra.layout.SimpleGrid
import com.github.mpetuska.khakra.layout.VStack
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
import size
import styled.*
import targetInputValue
import ui.ChakraTheme
import ui.common.Icon
import ui.khakra.SectionHeader
import ui.khakra.onChange
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
    VStack({w = arrayOf("100%", "100%", "70%"); spacing= arrayOf("32", "40", "48")}) {
        SearchBar("Search by name or URL") { props.onAction.invoke(SetupAction.UpdateSearch(it)) }
        MyPlaylists(props)
        SpotifyPlaylists(props)
    }
}

private fun RBuilder.SpotifyPlaylists(props: FindPlaylistsProps) {
    VStack({spacing= arrayOf("16", "24", "32"); width="100%"; alignItems="start"}) {
        SectionHeader { +"SPOTIFY PLAYLISTS" }
        PlaylistSearchResults(
            searchState = props.addPlaylistState.spotifySearchState,
            onTogglePlaylist = props.onAction,
        )
    }
}

private fun RBuilder.MyPlaylists(props: FindPlaylistsProps) {
    VStack({spacing= arrayOf("16", "24", "32"); width="100%"; alignItems="start"}) {
        SectionHeader { +"MY PLAYLISTS" }
        PlaylistSearchResults(
            searchState = props.addPlaylistState.myPlaylistSearchState,
            onTogglePlaylist = props.onAction,
        )
    }
}


private fun RBuilder.LogoutSpan() {
    styledSpan {
        css {
            color = theme.onBackgroundTernary
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
    InputGroup({variant="filled"; size="xl"}) {
        InputLeftElement() {
            Icon(ui.common.Icon.Search, color = ChakraTheme.onBackgroundSecondary)
/*            styledImg(src = ui.common.Icon.Search.resourcePath) {
                css {
                    size(32.px)
                    opacity = 0.5
                }
            }*/
        }
        Input({
            this["placeholder"] = placeholder
            onChange = { event ->
                onTermUpdate.invoke(event.targetInputValue)
            }
        })
    }
}

private fun RBuilder.PlaylistSearchResults(searchState: PlaylistSearchState, onTogglePlaylist: (SetupAction) -> Unit) {
    when(searchState) {
        is PlaylistSearchState.Results -> {
            SimpleGrid({minChildWidth="112px"; spacing= arrayOf("16", "20", "24"); w="100%"}) {
                searchState.playlists.forEach { (playlist, checked) ->
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