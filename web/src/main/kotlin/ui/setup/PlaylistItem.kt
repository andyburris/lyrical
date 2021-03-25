package ui.setup

import Size
import com.adamratzman.spotify.models.SimplePlaylist
import com.github.mpetuska.khakra.image.Image
import com.github.mpetuska.khakra.kt.get
import com.github.mpetuska.khakra.kt.set
import com.github.mpetuska.khakra.layout.*
import flexRow
import flexbox
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent
import recordOf
import size
import styled.css
import styled.styledDiv
import styled.styledImg
import styled.styledP
import ui.common.Icon
import ui.khakra.Body2
import ui.khakra.Subtitle2
import ui.khakra.onClick
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
    VStack({
        spacing="12"
        onClick = {
            println("playlist item clicked")
            props.onClick.invoke()
        }
        alignItems="start"
        `as` = "Button"
        textAlign = "left"
    }) {
        PlaylistImage(props.playlist, props.selected)
        PlaylistText(props.playlist)
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
    HStack({
        spacing = 16
        width = "100%"
        `as` = "Button"
        textAlign = "left"
        onClick = props.onClick
    }) {
        HStack({ spacing = 16; width = "100%" }) {
            Box({
                boxSize = arrayOf(40, 48)
                flexShrink = 0
            }) {
                PlaylistImage(props.playlist, props.selected)
            }
            PlaylistText(props.playlist)
        }
        Icon(Icon.Clear, alpha = 0.5) { flexShrink = 0 }
    }
}

private fun RBuilder.PlaylistImage(playlist: SimplePlaylist, selected: Boolean) {
    Center({
        borderRadius="2"
        val beforeProps = recordOf(
            "content" to "''",
            "paddingTop" to "100%"
        )
        this["_before"] = beforeProps
        transition = "background 200ms"
        bg = (if (selected)"linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5))," else "") + "url('${playlist.images.firstOrNull()?.url ?: "/assets/PlaylistPlaceholder.svg"}')"
        backgroundSize = "cover"
        width = "100%"
    }) {
        if (selected) {
            Icon(Icon.Check) { boxSize = "50%" }
        }
    }
}

private fun RBuilder.PlaylistText(playlist: SimplePlaylist) {
    VStack({ spacing = 0; width = "100%" }) {
        Subtitle2({ noOfLines = 2; width = "100%" }) {
            +playlist.name
        }
        Body2({ noOfLines = 2; width = "100%" }) {
            +(playlist.owner.displayName ?: "")
        }
    }
    flexbox(FlexDirection.column) {

    }
}

fun RBuilder.LoadingVerticalPlaylistItem() = child(loadingVerticalPlaylistItem) {}
val loadingVerticalPlaylistItem = functionalComponent<RProps> {
    flexbox(FlexDirection.column, gap = 12.px) {
        styledDiv {
            css {
                size(128.px)
                backgroundColor = theme.onBackgroundTernary
            }
        }
        styledDiv {
            css {
                backgroundColor = theme.onBackgroundTernary
                width = 64.px
                height = 24.px
            }
        }
    }
}