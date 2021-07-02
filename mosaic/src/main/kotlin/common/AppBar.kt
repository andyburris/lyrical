package common

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.TextStyle

@Composable
fun AppBar(
    title: String,
    subtitle: String? = null,
) {
    Column {
        Text(title)
        if (subtitle != null) {
            Text(subtitle, style = TextStyle.Dim)
        }
    }
}