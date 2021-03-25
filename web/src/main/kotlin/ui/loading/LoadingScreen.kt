package ui.loading

import LoadingState
import Screen
import animate
import com.github.mpetuska.khakra.layout.Box
import com.github.mpetuska.khakra.layout.HStack
import com.github.mpetuska.khakra.layout.VStack
import com.github.mpetuska.khakra.spinner.Spinner
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
import ui.khakra.SectionHeader
import ui.khakra.Subtitle1
import ui.khakra.colorTheme
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
    Box() {
        VStack({
            spacing = arrayOf(16, 24, 32)
            maxW = "1280px"
            alignItems = "start"
            p = arrayOf("32", "48", "64")
            minH = "100vh"
            justifyContent = "center"
            margin = "auto"
        }) {
            Heading1 { +"Loading..." }
            VStack({
                spacing = arrayOf(8, 12, 16)
                width = "100%"
                alignItems = "start"
            }) {
                indeterminateProgressBar()
                SectionHeader {
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
    Box({
        backgroundColor = colorTheme() + "onBackgroundTernary"
        width = "100%"
        height = arrayOf(4, 8)
        position = "relative"
        overflow = "hidden"
    }) {
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
            height = 100.pct
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