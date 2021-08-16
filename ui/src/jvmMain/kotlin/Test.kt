import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.platform.Typeface
import styles.implementation
import styles.text.FontStyle
import styles.text.FontWeight
import java.io.File

@Composable
fun Test() {
    val font = FontFamily(Font(file = File("YoungSerif.ttf"), weight = FontWeight.Normal.implementation, style = FontStyle.Normal.implementation))
    //val font2 = FontFamily(typeface = Typeface())
}