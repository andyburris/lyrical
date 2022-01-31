import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import compose.multiplatform.ui.text.FontStyle
import compose.multiplatform.ui.text.FontWeight
import styles.implementation
import java.io.File

@Composable
fun Test() {
    val font = FontFamily(Font(file = File("YoungSerif.ttf"), weight = FontWeight.Normal.implementation, style = FontStyle.Normal.implementation))
    //val font2 = FontFamily(typeface = Typeface())
    //Switch()
}