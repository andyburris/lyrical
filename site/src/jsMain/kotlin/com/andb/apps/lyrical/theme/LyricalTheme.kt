package com.andb.apps.lyrical.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.getColorMode
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Col

object LyricalTheme {
    val palette: LyricalPalette
        @Composable
        @ReadOnlyComposable
        get() = Colors.palettes[getColorMode()]!!

    fun paletteFrom(colorMode: ColorMode) = Colors.palettes[colorMode]!!

    object Colors {
        val windowPaletteLight = LyricalPalette(
            background = rgba("FFFFFF", 1.0),
            backgroundDark = rgba("F0F0F0", 1.0),
            backgroundCard = rgba("FFFFFF", 1.0),
            contentPrimary = rgba("000000", 0.87),
            contentSecondary = rgba("000000", 0.5),
            contentTertiary = rgba("000000", 0.25),
            divider = rgba("000000", 0.12),
            overlay = rgba("000000", 0.05),
            overlayDark = rgba("000000", 0.7),
        )
        val windowPaletteDark = LyricalPalette(
            background = rgba("333333", 1.0),
            backgroundDark = rgba("292929", 1.0),
            backgroundCard = rgba("545454", 1.0),
            contentPrimary = rgba("FFFFFF", 0.95),
            contentSecondary = rgba("FFFFFF", 0.5),
            contentTertiary = rgba("FFFFFF", 0.25),
            divider = rgba("FFFFFF", 0.12),
            overlay = rgba("FFFFFF", 0.05),
            overlayDark = rgba("000000", 0.7),
        )
        val accentPalette = LyricalPalette(
            background = rgba("1DB954"),
            backgroundDark = rgba("1BAA4D"),
            backgroundCard = rgba("1DB954"),
            contentPrimary = rgba("FFFFFF"),
            contentSecondary = rgba("FFFFFF", 0.65),
            contentTertiary = rgba("FFFFFF", 0.25),
            divider = rgba("FFFFFF", 0.2),
            overlay = rgba("FFFFFF", 0.12),
            overlayDark = rgba("000000", 0.7),
        )
        val palettes = mapOf(ColorMode.LIGHT to windowPaletteLight, ColorMode.DARK to windowPaletteDark)
    }

    object Spacing {
        val XS @Composable get() = responsiveSize(4.px, 8.px)
        val SM @Composable get() = responsiveSize(8.px, 12.px)
        val MD @Composable get() = responsiveSize(12.px, 16.px)
        val LG @Composable get() = responsiveSize(16.px, 24.px)
        val XL @Composable get() = responsiveSize(24.px, 32.px)
        val XXL @Composable get() = responsiveSize(32.px, 48.px)
        val XXXL @Composable get() = responsiveSize(48.px, 64.px)
    }

    object Size {
        object Icon {
            val Default @Composable get() = responsiveSize(24.px, 32.px)
        }
        object Button {
            val SM @Composable get() = responsiveSize(40.px, 48.px)
            val MD @Composable get() = responsiveSize(48.px, 56.px)
            val LG @Composable get() = responsiveSize(56.px, 64.px)
        }
        object Playlist {
            val CoverSm @Composable get() = responsiveSize(24.px, 32.px)
            val CoverMd @Composable get() = responsiveSize(40.px, 48.px)
            val CoverLg @Composable get() = responsiveSize(108.px, 128.px)
            object Placeholder {
                val Cover @Composable get() = responsiveSize(72.px, 96.px)
            }
        }

    }

    object Radii {
        val XS @Composable get() = responsiveSize(4.px, 8.px)
        val SM @Composable get() = responsiveSize(8.px, 12.px)
        val MD @Composable get() = responsiveSize(12.px, 16.px)
        val LG @Composable get() = responsiveSize(16.px, 24.px)
        val XL @Composable get() = responsiveSize(24.px, 32.px)
        val XXL @Composable get() = responsiveSize(32.px, 48.px)

        val Circle = 9999.px
    }
}

@Composable
private fun responsiveSize(mobile: CSSSizeValue<CSSUnit.px>, desktop: CSSSizeValue<CSSUnit.px>): CSSSizeValue<CSSUnit.px> = when(rememberBreakpoint()) {
    Breakpoint.ZERO -> mobile
    else -> desktop
}

private fun rgba(hexString: String, alpha: Double = 1.0) = Color.rgb(hexString.toInt(16)).copyf(alpha = alpha.toFloat())

data class LyricalPalette(
    val background: Color,
    val backgroundDark: Color,
    val backgroundCard: Color,
    val contentPrimary: Color,
    val contentSecondary: Color,
    val contentTertiary: Color,
    val divider: Color,
    val overlay: Color,
    val overlayDark: Color,
)

