@file:JvmName("PlatformImage")
package platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.core.graphics.implementation
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import java.net.URL

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
actual fun ActualImage(
    resource: Resource,
    contentDescription: String?,
    modifier: Modifier,
    placeholder: Resource?,
    tint: Color?,
) {
    val imageResource = when(resource) {
        is Resource.File -> io.kamel.core.Resource.Success(painterResource(resource.path))
        is Resource.Url -> lazyPainterResource(data = URL(resource.url))
    }
    KamelImage(
        resource = imageResource,
        contentDescription = contentDescription,
        modifier = modifier.implementation,
        colorFilter = tint?.let { ColorFilter.tint(it.implementation) }
    )
}