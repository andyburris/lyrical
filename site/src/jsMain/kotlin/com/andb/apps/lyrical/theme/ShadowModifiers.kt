package com.andb.apps.lyrical.theme

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color.Companion.rgba
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import org.jetbrains.compose.web.css.px

fun Modifier.outsetShadow(): Modifier {
    return this
        .boxShadow(0.px, 0.px, 0.px, 0.5.px, rgba(0f, 0f, 0f, 0.12f))
        .boxShadow(0.px, 2.px, 3.px, 0.px, rgba(0f, 0f, 0f, 0.12f))
        .boxShadow(0.px, 0.px, 4.px, 0.px, rgba(0f, 0f, 0f, 0.12f))
        .boxShadow(0.px, 1.px, 1.px, 0.px, rgba(1f, 1f, 1f, 0.12f), inset = true)
        .boxShadow(0.px, 0.px, 0.px, 1.px, rgba(1f, 1f, 1f, 0.05f), inset = true)
}