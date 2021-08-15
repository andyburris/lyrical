@file:JvmName("PlatformImage")
package platform

import androidx.compose.runtime.Composable
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.implementation
import java.net.URI

@Composable
actual fun ActualImage(
    uri: String,
    contentDescription: String?,
    modifier: Modifier,
    placeholderURI: String?
) {
    val imageResource = lazyPainterResource(data = URI(uri))
    KamelImage(
        resource = imageResource,
        contentDescription = contentDescription,
        modifier = modifier.implementation
    )
}