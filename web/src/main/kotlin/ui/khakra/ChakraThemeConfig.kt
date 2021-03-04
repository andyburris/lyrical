package ui.khakra

import com.soywiz.korio.toJsObject
import emptyRecord
import iterator
import kotlinext.js.Record
import map
import plus
import recordOf
import ui.ChakraColors

data class ChakraThemeConfig(
    val borders: Record<String, String> = recordOf(
        "none" to "0",
        "1px" to "1px solid",
        "2px" to "2px solid",
        "4px" to "4px solid",
        "8px" to "8px solid",
    ),
    val breakpoints: Record<String, String> = recordOf("sm" to "30em", "md" to "48em", "lg" to "62em", "xl" to "80em", "2xl" to "96em",),
    val colors: Record<String, ColorScheme> = recordOf(),
    val config: Config = Config(),
    val direction: String = "ltr",
    val radii: Record<String, String> = recordOf(
        "none" to "0",
        "sm" to "0.125rem",
        "base" to "0.25rem",
        "md" to "0.375rem",
        "lg" to "0.5rem",
        "xl" to "0.75rem",
        "2xl" to "1rem",
        "3xl" to "1.5rem",
        "full" to "9999px"
    ),
    val shadows: Record<String, String> = recordOf(
        "xs" to "0 0 0 1px rgba(0, 0, 0, 0.05)",
        "sm" to "0 1px 2px 0 rgba(0, 0, 0, 0.05)",
        "base" to "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)",
        "md" to "0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)",
        "lg" to "0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)",
        "xl" to "0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)",
        "2xl" to "0 25px 50px -12px rgba(0, 0, 0, 0.25)",
        "outline" to "0 0 0 3px rgba(66, 153, 225, 0.6)",
        "inner" to "inset 0 2px 4px 0 rgba(0,0,0,0.06)",
        "none" to "none",
        "dark-lg" to "rgba(0, 0, 0, 0.1) 0px 0px 0px 1px, rgba(0, 0, 0, 0.2) 0px 5px 10px, rgba(0, 0, 0, 0.4) 0px 15px 40px",
    ),
    val space: Record<String, String> = recordOf(
        "px" to "1px",
        "0" to "0",
        "0.5" to "0.125rem",
        "1" to "0.25rem",
        "1.5" to "0.375rem",
        "2" to "0.5rem",
        "2.5" to "0.625rem",
        "3" to "0.75rem",
        "3.5" to "0.875rem",
        "4" to "1rem",
        "5" to "1.25rem",
        "6" to "1.5rem",
        "7" to "1.75rem",
        "8" to "2rem",
        "9" to "2.25rem",
        "10" to "2.5rem",
        "12" to "3rem",
        "14" to "3.5rem",
        "16" to "4rem",
        "20" to "5rem",
        "24" to "6rem",
        "28" to "7rem",
        "32" to "8rem",
        "36" to "9rem",
        "40" to "10rem",
        "44" to "11rem",
        "48" to "12rem",
        "52" to "13rem",
        "56" to "14rem",
        "60" to "15rem",
        "64" to "16rem",
        "72" to "18rem",
        "80" to "20rem",
        "96" to "24rem",
    ),
    val sizes: Record<String, String> = recordOf(
        "max" to "max-content",
        "min" to "min-content",
        "full" to "100%",
        "3xs" to "14rem",
        "2xs" to "16rem",
        "xs" to "20rem",
        "sm" to "24rem",
        "md" to "28rem",
        "lg" to "32rem",
        "xl" to "36rem",
        "2xl" to "42rem",
        "3xl" to "48rem",
        "4xl" to "56rem",
        "5xl" to "64rem",
        "6xl" to "72rem",
        "7xl" to "80rem",
        "8xl" to "90rem",
    ),
    val styles: ChakraStyles = ChakraStyles { emptyRecord() },
    val transition: Transition = Transition(),
    val typography: Typography = Typography(),
    val zIndices: Record<String, String> = recordOf(
        "hide" to "-1",
        "auto" to "auto",
        "base" to "0",
        "docked" to "10",
        "dropdown" to "1000",
        "sticky" to "1100",
        "banner" to "1200",
        "overlay" to "1300",
        "modal" to "1400",
        "popover" to "1500",
        "skipLink" to "1600",
        "toast" to "1700",
        "tooltip" to "1800",
    ),
    val layerStyles: Record<String, Record<String, String>> = emptyRecord(),
    val textStyles: Record<String, Record<String, String>> = emptyRecord(),
    val components: Record<String, ComponentStyle> = emptyRecord(),
)

fun ChakraThemeConfig.toRecord(): Record<String, Any> = recordOf(
    "borders" to this.borders,
    "breakpoints" to this.breakpoints,
    "colors" to this.colors.map { it.value.toRecord() },
    "config" to this.config,
    "direction" to this.direction,
    "radii" to this.radii,
    "shadows" to this.shadows,
    "space" to this.space,
    "sizes" to this.sizes,
    "styles" to this.styles,
    "transition" to this.transition,
    "zIndices" to this.zIndices,
    "layerStyles" to this.layerStyles,
    "textStyles" to this.textStyles,
    "components" to this.components.map { it.value.toRecord() },
) + typography.toRecord()

data class Config(
    val useSystemColorMode: Boolean = false,
    val initialColorMode: String = "light"
)

data class Transition(
    val property: Record<String, String> = recordOf(
        "common" to "background-color, border-color, color, fill, stroke, opacity, box-shadow, transform",
        "colors" to "background-color, border-color, color, fill, stroke",
        "dimensions" to "width, height",
        "position" to "left, right, top, bottom",
        "background" to "background-color, background-image, background-position",
    ),
    val easing: Record<String, String> = recordOf(
        "ease-in" to "cubic-bezier(0.4, 0, 1, 1)",
        "ease-out" to "cubic-bezier(0, 0, 0.2, 1)",
        "ease-in-out" to "cubic-bezier(0.4, 0, 0.2, 1)",
    ),
    val duration: Record<String, String> = recordOf(
        "ultra-fast" to "50ms",
        "faster" to "100ms",
        "fast" to "150ms",
        "normal" to "200ms",
        "slow" to "300ms",
        "slower" to "400ms",
        "ultra-slow" to "500ms",
    )
)

data class Typography(
    val letterSpacings: Record<String, String> = recordOf(
        "tighter" to "-0.05em",
        "tight" to "-0.025em",
        "normal" to "0",
        "wide" to "0.025em",
        "wider" to "0.05em",
        "widest" to "0.1em",
    ),

    val lineHeights: Record<String, String> = recordOf(
        "normal" to "normal",
        "none" to "1",
        "shorter" to "1.25",
        "short" to "1.375",
        "base" to "1.5",
        "tall" to "1.625",
        "taller" to "2",
        "3" to ".75rem",
        "4" to "1rem",
        "5" to "1.25rem",
        "6" to "1.5rem",
        "7" to "1.75rem",
        "8" to "2rem",
        "9" to "2.25rem",
        "10" to "2.5rem",
    ),

    val fontWeights: Record<String, Int> = recordOf(
        "hairline" to 100,
        "thin" to 200,
        "light" to 300,
        "normal" to 400,
        "medium" to 500,
        "semibold" to 600,
        "bold" to 700,
        "extrabold" to 800,
        "black" to 900,
    ),

    val fonts: Record<String, String> = recordOf(
        "heading" to "-apple-system, BlinkMacSystemFont, \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\"",
        "body" to "-apple-system, BlinkMacSystemFont, \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\"",
        "mono" to "SFMono-Regular,Menlo,Monaco,Consolas,\"Liberation Mono\",\"Courier New\",monospace",
    ),

    val fontSizes: Record<String, String> = recordOf(
        "xs" to "0.75rem",
        "sm" to "0.875rem",
        "md" to "1rem",
        "lg" to "1.125rem",
        "xl" to "1.25rem",
        "2xl" to "1.5rem",
        "3xl" to "1.875rem",
        "4xl" to "2.25rem",
        "5xl" to "3rem",
        "6xl" to "3.75rem",
        "7xl" to "4.5rem",
        "8xl" to "6rem",
        "9xl" to "8rem",
    ),
)

fun Typography.toRecord() = recordOf(
    "letterSpacings" to letterSpacings,
    "lineHeights" to lineHeights,
    "fontWeights" to fontWeights,
    "fonts" to fonts,
    "fontSizes" to fontSizes,
)

interface ColorScheme {
    fun toRecord(): Record<String, String>
}

val customTheme = ChakraThemeConfig(
    colors = recordOf("brand" to ChakraColors()),
    config = Config(initialColorMode = "dark"),
    typography = Typography(
        fonts = recordOf(
            "heading" to "YoungSerif, -apple-system, BlinkMacSystemFont, \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\"",
            "body" to "Inter, -apple-system, BlinkMacSystemFont, \"Segoe UI\", Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\"",
            "mono" to "SFMono-Regular,Menlo,Monaco,Consolas,\"Liberation Mono\",\"Courier New\",monospace",
        ),
    ),
    components = recordOf(
        "Button" to ComponentStyle(
            baseStyle = recordOf(
                "borderRadius" to "full",
                "h" to "16"
            ),
            variants = recordOf(
                "solid" to recordOf(
                    "bg" to "brand.primary",
                    "color" to "brand.onPrimary",
                    "_hover" to recordOf(
                        "bg" to "brand.primaryDark",
                    ),
                ),
                "outline" to recordOf(
                    "border" to "2px solid",
                    "borderColor" to "brand.primary",
                    "color" to "brand.primary",
                    "_hover" to recordOf(
                        "borderColor" to "brand.primaryDark",
                        "color" to "brand.primaryDark",
                    )
                ),
            )
        )
    ),
    layerStyles = recordOf(
        "card" to recordOf(
            "backgroundColor" to "brand.backgroundDark",
            "color" to "brand.onBackground",
            "borderRadius" to "lg",
            "p" to "8",
        ),
        "primaryCard" to recordOf(
            "backgroundColor" to "brand.primary",
            "color" to "brand.onPrimary",
            "borderRadius" to "lg",
            "p" to "8",
        )
    ),
    textStyles = recordOf(
        "h1" to recordOf(
            "fontFamily" to "YoungSerif",
            "color" to "brand.onBackground",
            "fontWeight" to "500",
        ),
        "h2" to recordOf(
            "fontFamily" to "YoungSerif",
            "color" to "brand.onBackground",
            "fontWeight" to "500",
        ),
        "sectionHeader" to recordOf(
            "fontFamily" to "Inter",
            "color" to "brand.onBackgroundSecondary",
            "fontWeight" to "bold",
        ),
        "subtitle1" to recordOf(
            "fontFamily" to "Inter",
            "color" to "brand.onBackground",
            "fontWeight" to "bold",
        ),
        "subtitle2" to recordOf(
            "fontFamily" to "Inter",
            "color" to "brand.onBackground",
            "fontWeight" to "bold",
            "lineHeight" to "100%",
        ),
        "body2" to recordOf(
            "fontFamily" to "Inter",
            "color" to "brand.onBackgroundSecondary",
            "lineHeight" to "100%",
        ),
    )
)
