package platform

import org.jetbrains.compose.common.ui.unit.sp
import styles.text.FontFamily
import styles.text.FontWeight
import styles.text.TextStyle
import styles.text.sp

actual fun lyricalPlatformTypography() = LyricalTypography(
    displayFont = FontFamily(alias = "YoungSerif", "/assets/fonts/YoungSerif.otf"),
    bodyFont = FontFamily(alias = "Inter", "/assets/fonts/InterVariable.ttf"),
    h1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        letterSpacing = 0.sp
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    )
)

actual fun lyricalPlatformShapes() = LyricalShapes(
    small = RectangleShape, //TODO: Add RoundedCornerShape(4.dp)
    medium = RectangleShape, //TODO: Add RoundedCornerShape(8.dp)
    large = RectangleShape, //TODO: Add RoundedCornerShape(16.dp)
)