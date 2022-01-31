package platform

import compose.multiplatform.ui.shape.RoundedCornerShape
import compose.multiplatform.ui.text.FontFamily
import compose.multiplatform.ui.text.FontWeight
import compose.multiplatform.ui.text.TextStyle
import compose.multiplatform.ui.text.sp
import compose.multiplatform.ui.unit.dp
import compose.multiplatform.ui.unit.sp

private val displayFont = FontFamily(alias = "YoungSerif", "ui/src/jvmMain/resources/fonts/YoungSerif.otf")
private val bodyFont = FontFamily(alias = "Inter", "ui/src/jvmMain/resources/fonts/InterVariable.ttf")
actual fun lyricalPlatformTypography() = LyricalTypography(
    displayFont = displayFont,
    bodyFont = bodyFont,
    h1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 42.sp,
        fontFamily = displayFont,
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        fontFamily = displayFont,
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        fontFamily = displayFont,
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.15.sp,
        fontFamily = bodyFont, //TODO: variable fonts
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        //fontFamily = bodyFont, TODO: variable fonts
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        //fontFamily = bodyFont, TODO: variable fonts
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        //fontFamily = bodyFont, TODO: variable fonts
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        //fontFamily = bodyFont, TODO: variable fonts
    ),
)

actual fun lyricalPlatformShapes() = LyricalShapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp),
)