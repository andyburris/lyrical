package platform

import androidx.compose.runtime.Composable
import org.jetbrains.compose.common.core.graphics.Color
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.asAttributeBuilderApplier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.selectors.CSSSelector
import org.jetbrains.compose.web.css.selectors.className
import org.jetbrains.compose.web.css.selectors.plus
import org.jetbrains.compose.web.css.selectors.selector
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.css.CSSStyleSheet

@Composable
actual fun ActualImage(
    resource: Resource,
    contentDescription: String?,
    modifier: Modifier,
    placeholder: Resource?,
    tint: Color?,
) {
    Div { }
    val resourceURI = when(resource) {
        is Resource.File -> resource.path
        is Resource.Url -> resource.url
    }
    when (resourceURI.takeLast(4)) {
        ".svg" -> TagElement(
            elementBuilder = ElementBuilder.createBuilder("svg"),
            applyAttrs = {
                apply(modifier.asAttributeBuilderApplier())
                style {
                    if (tint != null) {
                        property("fill", rgb(tint.red, tint.blue, tint.green)) //TODO: replace with rgba, Color.implementation
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
            alt = contentDescription ?: "",
            attrs = modifier.asAttributeBuilderApplier()
        )
    }
}