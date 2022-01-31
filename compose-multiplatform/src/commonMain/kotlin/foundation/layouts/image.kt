package compose.multiplatform.foundation

import androidx.compose.runtime.Composable
import compose.multiplatform.ui.Color
import compose.multiplatform.ui.Modifier

@Composable
expect fun Image(
    resource: Resource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Resource? = null,
    tint: Color? = null,
)

sealed class Resource {
    data class File(val path: String) : Resource()
    data class Url(val url: String) : Resource()
}