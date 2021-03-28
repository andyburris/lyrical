package ui

import ui.khakra.ColorScheme
import kotlinx.css.*
import kotlinx.css.properties.BoxShadows
import kotlinx.css.properties.boxShadow
import recordOf
import ui.khakra.colorTheme

object ChakraTheme {
    val background: String get() = colorTheme() + "background"
    val backgroundDark: String get() = colorTheme() + "backgroundDark"
    val backgroundCard: String get() = colorTheme() + "backgroundCard"
    val onBackground: String get() = colorTheme() + "onBackground"
    val onBackgroundSecondary: String get() = colorTheme() + "onBackgroundSecondary"
    val onBackgroundTernary: String get() = colorTheme() + "onBackgroundTernary"
    val primary: String get() = colorTheme() + "primary"
    val primaryDark: String get() = colorTheme() + "primaryDark"
    val primaryLight: String get() = colorTheme() + "primaryLight"
    val onPrimary: String get() = colorTheme() + "onPrimary"
    val onPrimarySecondary: String get() = colorTheme() + "onPrimarySecondary"
    val onPrimaryTernary: String get() = colorTheme() + "onPrimaryTernary"
    val overlay: String get() = colorTheme() + "overlay"
    val darkOverlay: String get() = colorTheme() + "darkOverlay"
}

data class ChakraColors(
    val background: String = "#333333",
    val backgroundDark: String = "#2D2D2D",
    val backgroundCard: String = "#545454",
    val onBackground: String = "#FFFFFF",
    val onBackgroundSecondary: String = "rgba(255, 255, 255, .5)",
    val onBackgroundTernary: String = "rgba(255, 255, 255, .12)",
    val primary: String = "#1DB954",
    val primaryDark: String = "#1BAA4D",
    val primaryLight: String = "#38C169",
    val onPrimary: String = "#FFFFFF",
    val onPrimarySecondary: String = "rgba(255, 255, 255, .5)",
    val onPrimaryTernary: String = "rgba(255, 255, 255, .20)",
    val overlay: String = "rgba(0, 0, 0, .12)",
    val darkOverlay: String = "rgba(0, 0, 0, .70)",
) : ColorScheme {
    override fun toRecord() = recordOf(
        "background" to background,
        "backgroundDark" to backgroundDark,
        "backgroundCard" to backgroundCard,
        "onBackground" to onBackground,
        "onBackgroundSecondary" to onBackgroundSecondary,
        "onBackgroundTernary" to onBackgroundTernary,
        "primary" to primary,
        "primaryDark" to primaryDark,
        "primaryLight" to primaryLight,
        "onPrimary" to onPrimary,
        "onPrimarySecondary" to onPrimarySecondary,
        "onPrimaryTernary" to onPrimaryTernary,
        "overlay" to overlay,
        "darkOverlay" to darkOverlay,
    )
}

fun ChakraColors.toTheme() = Theme(
    background = Color(background),
    backgroundDark = Color(backgroundDark),
    backgroundCard = Color(backgroundCard),
    onBackground = Color(onBackground),
    onBackgroundSecondary = Color(onBackgroundSecondary),
    onBackgroundTernary = Color(onBackgroundTernary),
    primary = Color(primary),
    primaryDark = Color(primaryDark),
    primaryLight = Color(primaryLight),
    onPrimary = Color(onPrimary),
    onPrimarySecondary = Color(onPrimarySecondary),
    onPrimaryTernary = Color(onPrimaryTernary),
    overlay = Color(overlay),
    darkOverlay = Color(darkOverlay),
)

data class Theme(
    val background: Color = Color("#333333"),
    val backgroundDark: Color = Color("#2D2D2D"),
    val backgroundCard: Color = Color("#545454"),
    val onBackground: Color = Color("#FFFFFF"),
    val onBackgroundSecondary: Color = Color("rgba(255, 255, 255, .5)"),
    val onBackgroundTernary: Color = Color("rgba(255, 255, 255, .12)"),
    val primary: Color = Color("#1DB954"),
    val primaryDark: Color = Color("#1BAA4D"),
    val primaryLight: Color = Color("#38C169"),
    val onPrimary: Color = Color("#FFFFFF"),
    val onPrimarySecondary: Color = Color("rgba(255, 255, 255, .5)"),
    val onPrimaryTernary: Color = Color("rgba(255, 255, 255, .20)"),
    val overlay: Color = Color("rgba(0, 0, 0, .12)"),
    val darkOverlay: Color = Color("rgba(0, 0, 0, .70)"),
)

val theme = ChakraColors().toTheme()

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