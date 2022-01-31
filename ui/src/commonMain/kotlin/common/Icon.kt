package common

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.Image
import compose.multiplatform.foundation.Resource
import compose.multiplatform.ui.Color
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.unit.dp
import org.jetbrains.compose.common.ui.size
import platform.CurrentPalette

@Composable
fun Icon(
    icon: Icon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = CurrentPalette.contentPrimary,
) {
    Image(resource = Resource.File(icon.path), contentDescription = contentDescription, modifier = modifier.size(size), tint = tint)
}

sealed class Icon {
    object Check : Icon()
    object CheckCircle : Icon()
    object Person : Icon()
    object Link : Icon()
    object Play : Icon()
    object PlayCircle : Icon()
    object Skip : Icon()
    object Search : Icon()
    object Clear : Icon()
    object Options : Icon()
    object Join : Icon()
    object MoreVert : Icon()
    object Share : Icon()
    object Quote : Icon()
    object Note : Icon()
    object Playlist : Icon()
    object Hourglass : Icon()
    sealed class Arrow : Icon() {
        object Up : Arrow()
        object Down : Arrow()
        object Left : Arrow()
        object Right : Arrow()
    }
}

val Icon.name get() = when(this) {
    Icon.Check -> "Check"
    Icon.CheckCircle -> "CheckCircle"
    Icon.Person -> "Person"
    Icon.Link -> "Link"
    Icon.Play -> "Play"
    Icon.PlayCircle -> "PlayCircle"
    Icon.Skip -> "Skip"
    Icon.Search -> "Search"
    Icon.Clear -> "Clear"
    Icon.Options -> "Options"
    Icon.Join -> "Join"
    Icon.MoreVert -> "MoreVert"
    Icon.Share -> "Share"
    Icon.Quote -> "Quote"
    Icon.Note -> "Note"
    Icon.Playlist -> "Playlist"
    Icon.Hourglass -> "Hourglass"
    Icon.Arrow.Up -> "ArrowUp"
    Icon.Arrow.Down -> "ArrowDown"
    Icon.Arrow.Left -> "ArrowLeft"
    Icon.Arrow.Right -> "ArrowRight"
}

val Icon.path get() = "icons/" + this.name + ".svg"