package compose.multiplatform.ui

import compose.multiplatform.ui.Color
import org.jetbrains.compose.web.css.rgba

val Color.implementation
    get() = rgba(red, green, blue, alpha)