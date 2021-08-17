package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.ui.Modifier

@Composable
fun Image(
    resource: Resource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Resource? = null,
    tint: Color? = null,
) = ActualImage(resource, contentDescription, modifier, placeholder, tint)

@Composable
expect fun ActualImage(
    resource: Resource,
    contentDescription: String?,
    modifier: Modifier,
    placeholder: Resource?,
    tint: Color? = null,
)

sealed class Resource {
    data class File(val path: String) : Resource()
    data class Url(val url: String) : Resource()
}