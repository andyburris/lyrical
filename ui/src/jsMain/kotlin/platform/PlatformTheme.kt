package platform

import compose.multiplatform.ui.shape.RectangleShape
import compose.multiplatform.ui.shape.RoundedCornerShape
import compose.multiplatform.ui.text.FontFamily
import compose.multiplatform.ui.text.FontWeight
import compose.multiplatform.ui.text.TextStyle
import compose.multiplatform.ui.text.sp
import compose.multiplatform.ui.unit.dp
import compose.multiplatform.ui.unit.sp

val bodyFont = FontFamily(alias = "Inter", "/assets/fonts/InterVariable.ttf")
val displayFont = FontFamily(alias = "YoungSerif", "/assets/fonts/YoungSerif.otf")

actual fun lyricalPlatformTypography() = LyricalTypography(
    displayFont = displayFont,
    bodyFont = bodyFont,
    h1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp,
        fontFamily = displayFont,
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp,
        fontFamily = displayFont,
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        letterSpacing = 0.sp,
        fontFamily = displayFont,
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
        fontFamily = bodyFont,
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
        fontFamily = bodyFont,
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp,
        fontFamily = bodyFont,
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
        fontFamily = bodyFont,
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp,
        fontFamily = bodyFont,
    )
)

actual fun lyricalPlatformShapes() = LyricalShapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp),
)