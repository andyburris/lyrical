package compose.multiplatform.foundation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import compose.multiplatform.ui.Color
import org.jetbrains.compose.common.core.graphics.implementation
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import java.net.URL

@Composable
actual fun Image(
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