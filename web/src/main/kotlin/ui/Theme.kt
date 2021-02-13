package ui

import kotlinx.css.*
import kotlinx.css.properties.BoxShadows
import kotlinx.css.properties.boxShadow

data class Theme(
    val background: Color = Color("#333333"),
    val backgroundDark: Color = Color("#2D2D2D"),
    val backgroundCard: Color = Color("#545454"),
    val onBackground: Color = Color("#FFFFFF"),
    val onBackgroundSecondary: Color = Color("rgba(255, 255, 255, .5)"),
    val onBackgroundPlaceholder: Color = Color("rgba(255, 255, 255, .12)"),
    val primary: Color = Color("#1DB954"),
    val onPrimary: Color = Color("#FFFFFF"),
    val onPrimarySecondary: Color = Color("rgba(255, 255, 255, .5)"),
    val onPrimaryOverlay: Color = Color("rgba(255, 255, 255, .20)"),
    val overlay: Color = Color("rgba(0, 0, 0, .12)"),
    val darkOverlay: Color = Color("rgba(0, 0, 0, .70)"),
)

val theme = Theme()

fun CSSBuilder.shadow(elevationDp: Int) {
    when(elevationDp.coerceAtMost(5)) {
        0 -> { boxShadow = BoxShadows.none }
        1 -> {
            boxShadow(rgba(0,0,0,0.12), offsetY = 1.px, blurRadius = 3.px)
            boxShadow(rgba(0,0,0,0.24), offsetY = 1.px, blurRadius = 2.px)
        }
        2 -> {
            boxShadow(rgba(0,0,0,0.16), offsetY = 3.px, blurRadius = 6.px)
            boxShadow(rgba(0,0,0,0.23), offsetY = 3.px, blurRadius = 6.px)
        }
        3 -> {
            boxShadow(rgba(0,0,0,0.19), offsetY = 10.px, blurRadius = 20.px)
            boxShadow(rgba(0,0,0,0.23), offsetY = 6.px, blurRadius = 6.px)
        }
        4 -> {
            boxShadow(rgba(0,0,0,0.25), offsetY = 14.px, blurRadius = 28.px)
            boxShadow(rgba(0,0,0,0.22), offsetY = 10.px, blurRadius = 10.px)
        }
        5 -> {
            boxShadow(rgba(0,0,0,0.30), offsetY = 19.px, blurRadius = 38.px)
            boxShadow(rgba(0,0,0,0.22), offsetY = 15.px, blurRadius = 12.px)
        }
    }

}