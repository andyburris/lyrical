package ui.common

import Size
import kotlinx.css.*
import react.RBuilder
import size
import styled.css
import styled.styledImg

sealed class Icon {
    object Add : Icon()
    object Skip : Icon()
    object Library : Icon()
    object List : Icon()
    object Search : Icon()
    object Login : Icon()
    object Check : Icon()
    object Clear : Icon()
    object Profile : Icon()
    object NextLine : Icon()
    sealed class Arrow : Icon() {
        object Up : Arrow()
        object Down : Arrow()
        object Left : Arrow()
        object Right : Arrow()
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
    Icon.Profile -> "Profile"
    Icon.NextLine -> "NextLine"
    Icon.Arrow.Up -> "ArrowUp"
    Icon.Arrow.Down -> "ArrowDown"
    Icon.Arrow.Left -> "ArrowLeft"
    Icon.Arrow.Right -> "ArrowRight"
} + ".svg"

fun RBuilder.Icon(icon: Icon, colorFilter: String? = null, alpha: Double = 1.0, size: Size = Size(32.px), css: CSSBuilder.() -> Unit = {}) {
    styledImg(src = icon.resourcePath) {
        css {
            size(size)
            opacity = alpha
            if (colorFilter != null) {
                filter = colorFilter
            }
            css.invoke(this)
        }
    }
}