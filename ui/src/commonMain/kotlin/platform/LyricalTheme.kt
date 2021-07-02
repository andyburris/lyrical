package platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import jetbrains.compose.common.shapes.Shape
import org.jetbrains.compose.common.core.graphics.Color
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
    CompositionLocalProvider(LocalColors provides colors, LocalTypography provides typography, LocalShapes provides shapes, content = content)
}

data class LyricalColors(
    val background: Color,
    val backgroundDark: Color,
    val backgroundCard: Color,
    val onBackground: Color,
    val onBackgroundSecondary: Color,
    val onBackgroundTernary: Color,
    val primary: Color,
    val primaryDark: Color,
    val onPrimary: Color,
    val onPrimarySecondary: Color,
    val onPrimaryTernary: Color,
    val overlay: Color,
    val overlayDark: Color,
)

fun lightColors() = LyricalColors(
    background = 0xFFFFFFFF.toColor(),
    backgroundDark = 0xFFF0F0F0.toColor(),
    backgroundCard = 0xFFFFFFFF.toColor(),
    onBackground = 0xFF000000.toColor(),
    onBackgroundSecondary = 0x80000000.toColor(),
    onBackgroundTernary = 0x33000000.toColor(),
    primary = 0xFF1DB954.toColor(),
    primaryDark = 0xFF1BAA4D.toColor(),
    onPrimary = 0xFFFFFFFF.toColor(),
    onPrimarySecondary = 0x80FFFFFF.toColor(),
    onPrimaryTernary = 0x40FFFFFF.toColor(),
    overlay = 0x0D000000.toColor(),
    overlayDark = 0xB3000000.toColor(),
)

data class LyricalTypography(
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