package com.andb.apps.lyrical.theme

import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.DisposableEffectScope
import com.varabyte.kobweb.compose.css.boxShadow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

val OutsetShadowStyle by ComponentStyle {
    base {
        Modifier.styleModifier {
            this
                .boxShadow("0px 0px 0px 0.5px rgba(0, 0, 0, 0.12), 0px 1px 1px 0px rgba(255, 255, 255, 0.12) inset, 0px 0px 0px 1px rgba(255, 255, 255, 0.05) inset, 0px 2px 3px 0px rgba(0, 0, 0, 0.12), 0px 0px 4px 0px rgba(0, 0, 0, 0.12)")

        }
    }
}

fun Modifier.onInitialized(
    onDisposeBlock: (DisposableEffectScope.(ref: Element) -> DisposableEffectResult)? = null,
    block: DisposableEffectScope.(ref: HTMLElement) -> Unit
) = this
    .attrsModifier {
        ref {
            if (it is HTMLElement) block(it)
            onDispose { onDisposeBlock?.invoke(this, it) }
        }
    }