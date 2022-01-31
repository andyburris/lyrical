package compose.multiplatform.foundation

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.Resource
import compose.multiplatform.ui.implementation
import compose.multiplatform.ui.Color
import org.jetbrains.compose.common.internal.modifierWrapper
import compose.multiplatform.ui.Modifier
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.TagElement

@Composable
actual fun Image(
    resource: Resource,
    contentDescription: String?,
    modifier: Modifier,
    placeholder: Resource?,
    tint: Color?,
) = modifierWrapper(modifier) {
    val resourceURI = when(resource) {
        is Resource.File -> resource.path
        is Resource.Url -> resource.url
    }
    when (resourceURI.takeLast(4)) {
        ".svg" -> TagElement(
            elementBuilder = ElementBuilder.createBuilder("svg"),
            applyAttrs = {
                style {
                    if (tint != null) {
                        property("fill", tint.implementation)
                    }
                }
            },
        ) {
            TagElement(
                elementBuilder = ElementBuilder.createBuilder("use"),
                applyAttrs = {
                    attr("xlink:href", resourceURI)
                },
                content = null
            )
        }
        else -> Img(
            src = resourceURI,
            alt = contentDescription ?: ""
        )
    }
}