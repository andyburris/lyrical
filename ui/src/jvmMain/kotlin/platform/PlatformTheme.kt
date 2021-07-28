package platform

import org.jetbrains.compose.common.ui.unit.sp
import styles.text.FontFamily
import styles.text.FontWeight
import styles.text.TextStyle
import styles.text.sp

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
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        letterSpacing = 0.15.sp,
        //fontFamily = bodyFont, TODO: variable fonts
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Medium,
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
    small = RectangleShape, //TODO: Add RoundedCornerShape(4.dp)
    medium = RectangleShape, //TODO: Add RoundedCornerShape(8.dp)
    large = RectangleShape, //TODO: Add RoundedCornerShape(16.dp)
)