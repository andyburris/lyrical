package platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import jetbrains.compose.common.shapes.Shape
import org.jetbrains.compose.common.core.graphics.Color
import styles.text.FontFamily
import styles.text.TextStyle
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
    //background = 0xFFFFFFFF.toColor(),
    //backgroundDark = 0xFFF0F0F0.toColor(),
    //backgroundCard = 0xFFFFFFFF.toColor(),
    //onBackground = 0xFF000000.toColor(),
    //onBackgroundSecondary = 0x80000000.toColor(),
    //onBackgroundTernary = 0x33000000.toColor(),
    //primary = 0xFF1DB954.toColor(),
    //primaryDark = 0xFF1BAA4D.toColor(),
    //onPrimary = 0xFFFFFFFF.toColor(),
    //onPrimarySecondary = 0x80FFFFFF.toColor(),
    //onPrimaryTernary = 0x40FFFFFF.toColor(),
    backgroundPalette = Palette(
        background = Color.White,
        backgroundDark = Color(0xF0, 0xF0, 0xF0),
        backgroundLight = Color.White,
        contentPrimary = Color(0x21, 0x21, 0x21),
        contentSecondary = Color(0x80, 0x80, 0x80),
        contentTernary = Color(0xCC, 0xCC, 0xCC),
    ),
    primaryPalette = Palette(
        background = Color(0x1D, 0xB9, 0x54),
        backgroundDark = Color(0x1B, 0xAA, 0x4D),
        backgroundLight = Color(0x1D, 0xB9, 0x54),
        contentPrimary = Color.White,
        contentSecondary = Color(0x8E, 0xDC, 0xAA),
        contentTernary = Color(0x56, 0xCB, 0x7F),
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

object RectangleShape : Shape