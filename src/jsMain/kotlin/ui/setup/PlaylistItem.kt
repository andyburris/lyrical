package ui.setup

import Size
import com.adamratzman.spotify.models.SimplePlaylist
import flexbox
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseDownFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import size
import styled.css
import styled.styledDiv
import styled.styledImg
import styled.styledP
import ui.common.Icon
import ui.theme

fun RBuilder.PlaylistItem(playlist: SimplePlaylist, selected: Boolean, onClick: () -> Unit) = child(playlistItem) {
    attrs {
        this.playlist = playlist
        this.selected = selected
        this.onClick = onClick
    }
}
external interface PlaylistProps : RProps {
    var playlist: SimplePlaylist
    var selected: Boolean
    var onClick: () -> Unit
}
val playlistItem = functionalComponent<PlaylistProps> { props ->
    flexbox(FlexDirection.column, gap = 12.px) {
        css {
            cursor = Cursor.pointer
            width = 128.px
        }
        flexbox {
            css {
                size(128.px)
            }
            styledImg(src = props.playlist.images.firstOrNull()?.url ?: "/assets/PlaylistPlaceholder.svg") {
                css {
                    borderRadius = 4.px
                    size(128.px)
                }

            }
            if (props.selected) {
                flexbox(justifyContent = JustifyContent.center, alignItems = Align.center) {
                    css {
                        position = Position.absolute
                        borderRadius = 4.px
                        size(128.px)
                        backgroundColor = Color.black.withAlpha(0.7)
                    }
                    Icon(Icon.Check, size = Size(64.px))
                }
            }
            attrs.onClickFunction = {
                println("playlist item clicked")
                props.onClick.invoke()
            }
        }
        flexbox(FlexDirection.column) {
            styledP {
                css {
                    color = theme.onBackground
                    fontSize = 18.px
                    overflow = Overflow.hidden
                    textOverflow = TextOverflow.ellipsis
                    put("display", "-webkit-box")
                    put("-webkit-line-clamp", "2")
                    put("-webkit-box-orient", "vertical")
                }
                +props.playlist.name
            }
            styledP {
                css {
                    color = theme.onBackgroundSecondary
                    fontSize = 18.px
                    fontWeight = FontWeight.w500
                    overflow = Overflow.hidden
                    textOverflow = TextOverflow.ellipsis
                    put("display", "-webkit-box")
                    put("-webkit-line-clamp", "2")
                    put("-webkit-box-orient", "vertical")
                }
                +(props.playlist.owner.displayName ?: "")
            }
        }
    }
}

fun RBuilder.LoadingPlaylistItem() = child(loadingPlaylistItem) {}
val loadingPlaylistItem = functionalComponent<RProps> { props ->
    flexbox(FlexDirection.column, gap = 12.px) {
        styledDiv {
            css {
                size(128.px)
                backgroundColor = theme.onBackgroundPlaceholder
            }
        }
        styledDiv {
            css {
                backgroundColor = theme.onBackgroundPlaceholder
                width = 64.px
                height = 24.px
            }
        }
    }
}