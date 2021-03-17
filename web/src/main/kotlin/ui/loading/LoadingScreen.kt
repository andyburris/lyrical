package ui.loading

import LoadingState
import Screen
import animate
import flexbox
import kotlinx.css.*
import kotlinx.css.properties.IterationCount
import kotlinx.css.properties.cubicBezier
import kotlinx.css.properties.s
import react.RBuilder
import react.RProps
import react.child
import react.dom.p
import react.functionalComponent
import styled.*
import tween
import ui.khakra.Heading1
import ui.khakra.Subtitle1
import ui.theme
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

fun RBuilder.LoadingScreen(loadingState: LoadingState) = child(loading) {
    attrs {
        this.loadingState = loadingState
    }
}

external interface LoadingProps : RProps {
    var loadingState: LoadingState
}

val loading = functionalComponent<LoadingProps> { props ->
    Screen {
        flexbox(FlexDirection.column, gap = 32.px) {
            Heading1 { +"Loading..." }
            flexbox(FlexDirection.column, gap = 16.px) {
                css { width = 100.pct }
                indeterminateProgressBar()
                Subtitle1 {
                    when (val state = props.loadingState) {
                        LoadingState.LoadingSongs -> +"LOADING SONGS"
                        is LoadingState.LoadingLyrics -> +"LOADING LYRICS"
                    }
                }
            }
        }
    }
}


private fun RBuilder.indeterminateProgressBar() {
    styledDiv {
        css {
            backgroundColor = theme.onBackgroundTernary
            width = 100.pct
            height = 8.px
            position = Position.relative
            overflow = Overflow.hidden
        }

        progressBarForeground {
            firstAnimation()
        }
        progressBarForeground {
            secondAnimation()
        }
    }
}

private fun RBuilder.progressBarForeground(animation: CSSBuilder.() -> Unit) {
    styledDiv {
        css {
            backgroundColor = theme.primary
            height = 8.px
            position = Position.absolute
            top = 0.px
            bottom = 0.px
            animation()
        }
    }
}

private fun CSSBuilder.firstAnimation() {
    animation(2.1.s, timing = cubicBezier(0.65, 0.815, 0.735, 0.395), iterationCount = IterationCount.infinite) {
        0 {
            left = (-35).pct;
            right = 100.pct;
        }
        60 {
            left = 100.pct;
            right = (-90).pct;
        }
        100 {
            left = 100.pct;
            right = (-90).pct;
        }
    }
    //MuiLinearProgress-keyframes-indeterminate1 2.1s cubic-bezier(0.65, 0.815, 0.735, 0.395) infinite
}

private fun CSSBuilder.secondAnimation() {
    animation(2.1.s, timing = cubicBezier(0.165, 0.84, 0.44, 1.0), delay = 1.15.s, iterationCount = IterationCount.infinite) {
        0 {
            left = (-200).pct;
            right = 100.pct;
        }
        60 {
            left = 107.pct;
            right = (-8).pct;
        }
        100 {
            left = 107.pct;
            right = (-8).pct;
        }
    }
    //MuiLinearProgress-keyframes-indeterminate2 2.1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.15s infinite
}

@OptIn(ExperimentalTime::class)
private fun RBuilder.determinateProgressBar(loadingState: LoadingState.LoadingLyrics) {
    styledDiv {
        css {
            backgroundColor = theme.onBackgroundTernary
            width = 100.pct
            height = 8.px
        }

        styledDiv {
            val progress = animate(loadingState.percent, animationSpec = tween(duration = 200.milliseconds)) { old, new, progress -> old + ((new - old) * progress) }
            css {
                backgroundColor = theme.primary
                height = 8.px
                width = (progress * 100).pct
            }
        }
    }
}