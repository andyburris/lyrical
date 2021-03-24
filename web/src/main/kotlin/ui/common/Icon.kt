package ui.common

import com.github.mpetuska.khakra.image.Image
import com.github.mpetuska.khakra.image.ImageProps
import react.RBuilder

sealed class Icon {
    object Add : Icon()
    object Skip : Icon()
    object Library : Icon()
    object List : Icon()
    object Search : Icon()
    object Login : Icon()
    object Check : Icon()
    object Clear : Icon()
    object Person : Icon()
    object NextLine : Icon()
    object Quote : Icon()
    sealed class Arrow : Icon() {
        object Up : Arrow()
        object Down : Arrow()
        object Left : Arrow()
        object Right : Arrow()
    }
    sealed class Platform : Icon() {
        object Android : Platform()
        object Apple : Platform()
        object Windows : Platform()
        object Desktop : Platform()
    }
}

val Icon.resourcePath get() = "/assets/icons/" + when(this) {
    Icon.Add -> "Add"
    Icon.Skip -> "Skip"
    Icon.Library -> "Library"
    Icon.List -> "List"
    Icon.Search -> "Search"
    Icon.Login -> "Login"
    Icon.Check -> "Check"
    Icon.Clear -> "Clear"
    Icon.Person -> "Profile"
    Icon.NextLine -> "NextLine"
    Icon.Quote -> "Quote"
    Icon.Arrow.Up -> "ArrowUp"
    Icon.Arrow.Down -> "ArrowDown"
    Icon.Arrow.Left -> "ArrowLeft"
    Icon.Arrow.Right -> "ArrowRight"
    Icon.Platform.Android -> "Platform/Android"
    Icon.Platform.Apple -> "Platform/Apple"
    Icon.Platform.Windows -> "Platform/Windows"
    Icon.Platform.Desktop -> "Platform/Desktop"
} + ".svg"

fun RBuilder.Icon(icon: Icon, colorFilter: String? = null, alpha: Double = 1.0, props: ImageProps.() -> Unit = {}) {
    Image({
        opacity = alpha
        if (colorFilter != null) {
            filter = colorFilter
        }
        boxSize = arrayOf("24", "32")
        src=icon.resourcePath
        props()
    })
}