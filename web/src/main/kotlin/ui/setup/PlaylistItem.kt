package ui.setup

import Size
import com.adamratzman.spotify.models.SimplePlaylist
import flexRow
import flexbox
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent
import size
import styled.css
import styled.styledDiv
import styled.styledImg
import styled.styledP
import ui.common.Icon
import ui.theme

fun RBuilder.VerticalPlaylistItem(playlist: SimplePlaylist, selected: Boolean, onClick: () -> Unit) = child(verticalPlaylistItem) {
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
val verticalPlaylistItem = functionalComponent<PlaylistProps> { props ->
    flexbox(FlexDirection.column, gap = 12.px) {
        css {
            cursor = Cursor.pointer
            width = 128.px
        }
        PlaylistImage(props.playlist, props.selected, 128.px)
        PlaylistText(props.playlist)
        attrs.onClickFunction = {
            println("playlist item clicked")
            props.onClick.invoke()
        }
    }
}

fun RBuilder.HorizontalPlaylistItem(playlist: SimplePlaylist, selected: Boolean, onClick: () -> Unit) = child(horizontalPlaylistItem) {
    attrs {
        this.playlist = playlist
        this.selected = selected
        this.onClick = onClick
    }
}
val horizontalPlaylistItem = functionalComponent<PlaylistProps> { props ->
    flexRow(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center, gap = 16.px) {
        css {
            cursor = Cursor.pointer
            width = 100.pct
        }
        flexRow(gap = 16.px, alignItems = Align.center) {
            PlaylistImage(props.playlist, props.selected, 56.px)
            PlaylistText(props.playlist)

        }
        div {
            Icon(Icon.Clear, alpha = 0.5)
            attrs.onClickFunction = {
                println("playlist item clicked")
                props.onClick.invoke()
            }
        }
    }
}

private fun RBuilder.PlaylistImage(playlist: SimplePlaylist, selected: Boolean, size: LinearDimension) {
    flexbox {
        css {
            size(size)
        }
        styledImg(src = playlist.images.firstOrNull()?.url ?: "/assets/PlaylistPlaceholder.svg") {
            css {
                borderRadius = 4.px
                size(size)
            }

        }
        if (selected) {
            flexbox(justifyContent = JustifyContent.center, alignItems = Align.center) {
                css {
                    position = Position.absolute
                    borderRadius = 4.px
                    size(size)
                    backgroundColor = Color.black.withAlpha(0.7)
                }
                Icon(Icon.Check, size = Size(size / 2))
            }
        }

    }
}

private fun RBuilder.PlaylistText(playlist: SimplePlaylist) {
    flexbox(FlexDirection.column) {
        styledP {
            css {
                color = theme.onBackground
                fontSize = 18.px
                overflow = Overflow.hidden
                textOverflow = TextOverflow.ellipsis
                wordWrap = WordWrap.breakWord
                put("overflow-wrap", "anywhere")
                put("display", "-webkit-box")
                put("-webkit-line-clamp", "2")
                put("-webkit-box-orient", "vertical")
            }
            +playlist.name
        }
        styledP {
            css {
                color = theme.onBackgroundSecondary
                fontSize = 18.px
                fontWeight = FontWeight.w500
                overflow = Overflow.hidden
                textOverflow = TextOverflow.ellipsis
                put("overflow-wrap", "anywhere")
                put("display", "-webkit-box")
                put("-webkit-line-clamp", "2")
                put("-webkit-box-orient", "vertical")
            }
            +(playlist.owner.displayName ?: "")
        }
    }
}

fun RBuilder.LoadingVerticalPlaylistItem() = child(loadingVerticalPlaylistItem) {}
val loadingVerticalPlaylistItem = functionalComponent<RProps> {
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