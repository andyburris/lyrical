package com.andb.apps.lyrical.pages.game.loading

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.CSSAnimation
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.animation.Keyframes
import com.varabyte.kobweb.silk.components.animation.toAnimation
import org.jetbrains.compose.web.css.*

@Composable
fun IndeterminateProgressBar(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .position(Position.Relative)
            .borderRadius(LyricalTheme.Radii.Circle)
            .overflow(Overflow.Hidden)
            .backgroundColor(LyricalTheme.palette.backgroundDark)
            .fillMaxWidth()
            .height(LyricalTheme.Spacing.XS)
    ) {
        AnimatedProgressBar(
            animation = ProgressBar1Keyframes.toAnimation(
                duration = 2.1.s,
                timingFunction = AnimationTimingFunction.cubicBezier(0.65, 0.815, 0.735, 0.395),
                iterationCount = AnimationIterationCount.Infinite,
            )
        )
        AnimatedProgressBar(
            animation = ProgressBar2Keyframes.toAnimation(
                duration = 2.1.s,
                timingFunction = AnimationTimingFunction.cubicBezier(0.165, 0.84, 0.44, 1.0),
                iterationCount = AnimationIterationCount.Infinite,
            )
        )
    }
}

@Composable
private fun AnimatedProgressBar(
    animation: CSSAnimation,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .top(0.px)
            .left(0.px)
            .position(Position.Absolute)
            .borderRadius(LyricalTheme.Radii.Circle)
            .height(LyricalTheme.Spacing.XS)
            .backgroundColor(LyricalTheme.Colors.accentPalette.background)
            .animation(animation)
    )
}


val ProgressBar1Keyframes by Keyframes {
    0.percent {
        Modifier
            .left((-35).percent)
            .right(100.percent)
    }
    60.percent {
        Modifier
            .left(100.percent)
            .right((-90).percent)
    }
    100.percent {
        Modifier
            .left(100.percent)
            .right((-90).percent)
    }
}

val ProgressBar2Keyframes by Keyframes {
    0.percent {
        Modifier
            .left((-200).percent)
            .right(100.percent)
    }
    60.percent {
        Modifier
            .left(107.percent)
            .right((-8).percent)
    }
    100.percent {
        Modifier
            .left(107.percent)
            .right((-8).percent)
    }
}