package com.andb.apps.lyrical

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.px

object LyricalTheme {
    object Colors {

    }

    object Spacing {
        val XS @Composable get() = responsiveSize(4.px, 8.px)
        val SM @Composable get() = responsiveSize(8.px, 12.px)
        val MD @Composable get() = responsiveSize(12.px, 16.px)
        val LG @Composable get() = responsiveSize(16.px, 24.px)
        val XL @Composable get() = responsiveSize(24.px, 32.px)
        val XXL @Composable get() = responsiveSize(32.px, 48.px)
    }

    object Size {
        object Button {
            val SM @Composable get() = responsiveSize(40.px, 48.px)
            val MD @Composable get() = responsiveSize(48.px, 56.px)
            val LG @Composable get() = responsiveSize(56.px, 64.px)
        }
    }

    object Radii {
        val Circle = 9999.px
    }
}

@Composable
private fun responsiveSize(mobile: CSSSizeValue<CSSUnit.px>, desktop: CSSSizeValue<CSSUnit.px>): CSSSizeValue<CSSUnit.px> = when(rememberBreakpoint()) {
    Breakpoint.ZERO -> mobile
    else -> desktop
}