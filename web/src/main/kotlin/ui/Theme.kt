package ui

import kotlinx.css.*
import kotlinx.css.properties.BoxShadows
import kotlinx.css.properties.boxShadow
import recordOf
import ui.khakra.*

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

fun customTheme() = createTheme {
    val subtitle1 = styleBlock {
        "fontFamily" to "Inter"
        "fontSize".toBreakpoints("1rem", "1.25rem", "1.5rem")
        "fontWeight" to "bold"
        "lineHeight" to "115%"
    }
    val heading1 = styleBlock {
        "fontFamily" to "YoungSerif"
        "fontWeight" to "500"
        "fontSize".toBreakpoints("2.5rem", "3rem", "4rem")
        "lineHeight" to "115%"
    }
    val heading2 = styleBlock {
        "fontFamily" to "YoungSerif"
        "fontWeight" to "500"
        "fontSize".toBreakpoints("1.5rem", "2rem", "2.5rem")
        "lineHeight" to "115%"
    }
    val fab = styleBlock {
        "borderRadius" to "full"
        "height".toBreakpoints("48", "56", "64")
        "px" to "24"
        subtitle1()
        "zIndex" to "docked"
    }
    breakpoints {
        "md" to "60em"
        "lg" to "70em"
    }
    colors {
        "brandLight" toObject {
            "background" to "#FFFFFF"
            "backgroundDark" to "#F0F0F0"
            "backgroundCard" to "#FFFFFF"
            "onBackground" to "rgba(0, 0, 0, .87)"
            "onBackgroundSecondary" to "rgba(0, 0, 0, .5)"
            "onBackgroundTernary" to "rgba(0, 0, 0, .20)"
            "primary" to "#1DB954"
            "primaryDark" to "#1BAA4D"
            "primaryLight" to "#38C169"
            "onPrimary" to "#FFFFFF"
            "onPrimarySecondary" to "rgba(255, 255, 255, .5)"
            "onPrimaryTernary" to "rgba(255, 255, 255, .20)"
            "overlay" to "rgba(0, 0, 0, .05)"
            "darkOverlay" to "rgba(0, 0, 0, .70)"
        }
        "brandDark" toObject {
            "background" to "#333333"
            "backgroundDark" to "#2D2D2D"
            "backgroundCard" to "#545454"
            "onBackground" to "#FFFFFF"
            "onBackgroundSecondary" to "rgba(255, 255, 255, .5)"
            "onBackgroundTernary" to "rgba(255, 255, 255, .20)"
            "primary" to "#1DB954"
            "primaryDark" to "#1BAA4D"
            "primaryLight" to "#38C169"
            "onPrimary" to "#FFFFFF"
            "onPrimarySecondary" to "rgba(255, 255, 255, .5)"
            "onPrimaryTernary" to "rgba(255, 255, 255, .20)"
            "overlay" to "rgba(0, 0, 0, .12)"
            "darkOverlay" to "rgba(0, 0, 0, .70)"
        }
    }
    config {
        initialColorMode = ColorMode.Dark
    }
    components {
        "Button" toSinglePartComponent {
            variants {
                "solid" toReactive { props ->
                    "bg" to props.colorMode.value("brandLight.primary", "brandDark.primary")
                    "color" to props.colorMode.value("brandLight.onPrimary", "brandDark.onPrimary")
                    "fill" to props.colorMode.value("brandLight.onPrimary", "brandDark.onPrimary")
                    "_hover" toObject {
                        "bg" to props.colorMode.value("brandLight.primaryDark", "brandDark.primaryDark")
                    }
                    "_active" toObject {
                        "bg" to props.colorMode.value("brandLight.primaryDark", "brandDark.primaryDark")
                    }
                }
                "solidBackground" toReactive { props ->
                    "bg" to props.colorMode.value("brandLight.background", "brandDark.background")
                    "color" to props.colorMode.value("brandLight.onBackground", "brandDark.onBackground")
                    "fill" to props.colorMode.value("brandLight.onBackground", "brandDark.onBackground")
                    "_hover" toObject {
                        "bg" to props.colorMode.value("brandLight.backgroundDark", "brandDark.backgroundDark")
                    }
                    "_active" toObject {
                        "bg" to props.colorMode.value("brandLight.backgroundDark", "brandDark.backgroundDark")
                    }
                }
                "solidCard" toReactive { props ->
                    "bg" to props.colorMode.value("brandLight.backgroundCard", "brandDark.backgroundCard")
                    "color" to props.colorMode.value("brandLight.onBackground", "brandDark.onBackground")
                    "fill" to props.colorMode.value("brandLight.onBackground", "brandDark.onBackground")
                    "_hover" toObject {
                        "bg" to props.colorMode.value("brandLight.background", "brandDark.background")
                    }
                    "_active" toObject {
                        "bg" to props.colorMode.value("brandLight.background", "brandDark.background")
                    }
                }
                "outline" toReactive { props ->
                    "border" to "none"
                    "boxShadow" to "inset 0px 0px 0px 1px " + props.colorMode.value("rgba(0,0,0,0.20)", "rgba(255,255,255,0.20)")
                    "color" to props.colorMode.value("brandLight.onBackgroundSecondary", "brandDark.onBackgroundSecondary")
                    "fill" to props.colorMode.value("brandLight.onBackgroundSecondary", "brandDark.onBackgroundSecondary")
                    "_hover" toObject {
                        "bg" to props.colorMode.value("brandLight.backgroundDark", "brandDark.backgroundDark")
                    }
                }
            }
            sizes {
                "fab" toObject {
                    fab()
                    "bottom".toBreakpoints("32", "0")
                    "right".toBreakpoints("32", "0")
                    "position".toBreakpoints("fixed", "static")
                }
                "fabStatic" toObject {
                    fab()
                }
                "chip" toObject {
                    "borderRadius" to "full"
                    "fontSize".toBreakpoints("1rem", "1.5rem")
                    "px".toBreakpoints("8", "12")
                    "py" to "4"
                }
            }
        }
        "Input".toMultiPartComponent(listOf("field", "addon")) {
            variants {
                "filled" toObject { props ->
                    "field" toObject {
                        "bg" to props.colorMode.value("brandLight.backgroundDark", "brandDark.backgroundCard")
                        "borderRadius" to "full"
                    }
                }
                "unstyled" toObject {
                    "field" toObject {
                        "background" to "transparent"
                    }
                }
            }
            sizes {
                "sm" toObject {
                    "px" to "2"
                }
                "lg" toObject {
                    "field" toObject {
                        "h" to "3rem"
                        subtitle1()
                    }
                    "addon" toObject {
                        "h" to "48"
                        "pl" to "8"
                    }
                }
                "xl" toObject {
                    "field" toObject {
                        "h".toBreakpoints("3rem", "4rem")
                        subtitle1()
                    }
                    "addon" toObject {
                        "h" to "4rem"
                        "w" to "4rem"
                        "pl" to "1rem"
                        "pr" to "0rem"
                    }
                }
                "unstyled" toObject {
                    "field" toObject {
                        heading1()
                    }
                }
            }
        }
    }
    radii {
        "0" to "0"
        "2" to "0.125rem"
        "4" to "0.25rem"
        "8" to "0.5rem"
        "12" to "0.75"
        "16" to "1rem"
        "20" to "1.25rem"
        "24" to "1.5rem"
        "32" to "2rem"
        "40" to "2.5rem"
        "48" to "3rem"
        "56" to "3.5rem"
        "64" to "4rem"
        "72" to "4.5rem"
        "80" to "5rem"
        "88" to "5.5rem"
        "96" to "6rem"
        "112" to "7rem"
        "128" to "8rem"
    }
    space {
        "0" to "0"
        "2" to "0.125rem"
        "4" to "0.25rem"
        "8" to "0.5rem"
        "12" to "0.75rem"
        "16" to "1rem"
        "20" to "1.25rem"
        "24" to "1.5rem"
        "32" to "2rem"
        "40" to "2.5rem"
        "48" to "3rem"
        "56" to "3.5rem"
        "64" to "4rem"
        "72" to "4.5rem"
        "80" to "5rem"
        "88" to "5.5rem"
        "96" to "6rem"
        "112" to "7rem"
        "128" to "8rem"
    }
    layerStyles {
        "card" toObject {
            "backgroundColor".toReactive("brandLight.backgroundDark", "brandDark.backgroundDark")
/*                    ".chakra-ui-dark &" toObject {
                        "backgroundColor" to "brandDark.backgroundDark"
                        "color" to "brandDark.onBackground"
                    }*/
            "color".toReactive("brandLight.onBackground", "brandDark.onBackground")
            "fill".toReactive("brandLight.onBackground", "brandDark.onBackground")
            "borderRadius" to "16"
            "p".toBreakpoints("24", "32")
            "transition" to "background-color 200ms"
        }
        "primaryCard" toObject {
            "backgroundColor" to "brandDark.primary"
            "color" to "brandDark.onPrimary"
            "fill" to "brandDark.onPrimary"
            "borderRadius" to "16"
            "p".toBreakpoints("24", "32")
        }
    }
    textStyles {
        "h1" toObject {
            heading1()
        }
        "h2" toObject {
            heading2()
        }
        "sectionHeader" toObject {
            subtitle1()
            "fontWeight" to "bold"
        }
        "subtitle1" toObject {
            subtitle1()
        }
        "subtitle2" toObject {
            "fontFamily" to "Inter"
            "fontWeight" to "bold"
            "lineHeight" to "115%"
            "fontSize".toBreakpoints("0.875rem", "1rem", "1.25rem")
        }
        "body2" toObject {
            "fontFamily" to "Inter"
            "lineHeight" to "115%"
            "fontSize".toBreakpoints("0.875rem", "1rem", "1.25rem")
        }
    }
    globalStyles { props ->
        "body" toObject {
            "bgColor" to if (props.colorMode == ColorMode.Dark) "brandDark.background" else "brandLight.background"
        }
    }
    typography {
        fonts {
            "heading" to "YoungSerif, -apple-system, BlinkMacSystemFont, \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\""
            "body" to "Inter, -apple-system, BlinkMacSystemFont, \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\""
            "mono" to "SFMono-Regular,Menlo,Monaco,Consolas,\"Liberation Mono\",\"Courier New\",monospace"
        }
    }
}