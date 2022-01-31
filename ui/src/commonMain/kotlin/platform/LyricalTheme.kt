package platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import compose.multiplatform.ui.Color
import compose.multiplatform.ui.shape.Shape
import compose.multiplatform.ui.text.FontFamily
import compose.multiplatform.ui.text.TextStyle
import toColor


val LocalColors = staticCompositionLocalOf { lightColors() }
val LocalTypography = staticCompositionLocalOf { lyricalPlatformTypography() }
val LocalShapes = staticCompositionLocalOf { lyricalPlatformShapes() }
object LyricalTheme {
    val colors: LyricalColors
        @Composable get() = LocalColors.current
    val typography: LyricalTypography
        @Composable get() = LocalTypography.current
    val shapes: LyricalShapes
        @Composable get() = LocalShapes.current
}

@Composable
fun LyricalTheme(
    colors: LyricalColors = LocalColors.current,
    typography: LyricalTypography = LocalTypography.current,
    shapes: LyricalShapes = LocalShapes.current,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalColors provides colors, LocalTypography provides typography, LocalShapes provides shapes, LocalPalette provides colors.backgroundPalette, content = content)
}

data class LyricalColors(
    val backgroundPalette: Palette,
    val primaryPalette: Palette,
    val overlay: Color,
    val overlayDark: Color,
) {
    val background: Color = backgroundPalette.background
    val backgroundDark: Color = backgroundPalette.backgroundDark
    val backgroundCard: Color = backgroundPalette.backgroundLight
    val onBackground: Color = backgroundPalette.contentPrimary
    val onBackgroundSecondary: Color = backgroundPalette.contentSecondary
    val onBackgroundTernary: Color = backgroundPalette.contentTernary
    val primary: Color = primaryPalette.background
    val primaryDark: Color = primaryPalette.backgroundDark
    val onPrimary: Color = primaryPalette.contentPrimary
    val onPrimarySecondary: Color = primaryPalette.contentSecondary
    val onPrimaryTernary: Color = primaryPalette.contentTernary
}

data class Palette(
    val background: Color,
    val backgroundDark: Color,
    val backgroundLight: Color,
    val contentPrimary: Color,
    val contentSecondary: Color,
    val contentTernary: Color,
)

val CurrentPalette @Composable get() = LocalPalette.current
val LocalPalette = staticCompositionLocalOf { lightColors().backgroundPalette }
@Composable
fun ProvidePalette(palette: Palette, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalPalette provides palette, content = content)
}
@Composable
fun Palette.invert() = if (this == LyricalTheme.colors.backgroundPalette) LyricalTheme.colors.primaryPalette else LyricalTheme.colors.backgroundPalette

fun lightColors() = LyricalColors(
    backgroundPalette = Palette(
        background = 0xFFFFFFFF.toColor(),
        backgroundDark = 0xFFF0F0F0.toColor(),
        backgroundLight = 0xFFFFFFFF.toColor(),
        contentPrimary = 0xDE000000.toColor(),
        contentSecondary = 0x80000000.toColor(),
        contentTernary = 0x33000000.toColor(),
    ),
    primaryPalette = Palette(
        background = 0xFF1DB954.toColor(),
        backgroundDark = 0xFF1BAA4D.toColor(),
        backgroundLight = Color(0x1D, 0xB9, 0x54),
        contentPrimary = 0xFFFFFFFF.toColor(),
        contentSecondary = 0x80FFFFFF.toColor(),
        contentTernary = 0x40FFFFFF.toColor(),
    ),
    overlay = 0x0D000000.toColor(),
    overlayDark = 0xB3000000.toColor(),
)

data class LyricalTypography(
    val displayFont: FontFamily,
    val bodyFont: FontFamily,
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val subtitle1: TextStyle,
    val subtitle2: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val caption: TextStyle,
)

data class LyricalShapes(
    val small: Shape,
    val medium: Shape,
    val large: Shape,
)