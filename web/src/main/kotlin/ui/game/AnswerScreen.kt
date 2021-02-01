package ui.game

import com.adamratzman.spotify.models.Track
import imageUrl
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.functionalComponent
import styled.*
import react.child
import react.dom.*
import ui.theme

fun RBuilder.AnswerScreen(state: State.GameState.Answer, onNextScreen: () -> Unit) = child(answerScreen) {
    attrs {
        this.state = state
        this.onNextScreen = onNextScreen
    }
}

external interface AnswerProps : RProps {
    var state: State.GameState.Answer
    var onNextScreen: () -> Unit
}

val answerScreen = functionalComponent<AnswerProps> { props ->
    styledDiv {
        css {
            width = 100.pct
            minHeight = 100.vh
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = Align.center
            padding(vertical = 128.px, horizontal = 196.px)
            boxSizing = BoxSizing.borderBox
            backgroundColor = if (props.state.answer is GameAnswer.Correct) theme.primary else theme.background
            gap = Gap("64px")
        }
        h1 {
            when(props.state.answer) {
                is GameAnswer.Correct -> +"Correct!"
                is GameAnswer.Incorrect -> +"Incorrect"
                is GameAnswer.Skipped -> +"Skipped"
            }
        }

        styledDiv {
            css {
                backgroundColor = theme.onBackgroundPlaceholder
                padding(48.px)
                borderRadius = 16.px
                display = Display.flex
                flexDirection = FlexDirection.column
                gap = Gap("32px")
                width = 100.pct
            }
            when (val answer = props.state.answer) {
                is GameAnswer.Correct -> {
                    SongItem(props.state.track.track)
                }
                is GameAnswer.Incorrect -> {
                    YourAnswer(answer.answer)
                    CorrectAnswer(props.state.track.track)
                }
                is GameAnswer.Skipped -> {
                    CorrectAnswer(props.state.track.track)
                }
            }
        }

        styledDiv {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                gap = Gap("32px")
            }

            styledButton {
                css {
                    backgroundColor = if (props.state.answer is GameAnswer.Correct) theme.background else theme.primary
                }
                attrs {
                    onClickFunction = {
                        props.onNextScreen.invoke()
                    }
                }
                when(props.state.questionNumber == props.state.data.questions.size - 1) {
                    false -> +"Next Question".toUpperCase()
                    true -> +"End Game".toUpperCase()
                }

            }

            GameState(props.state.questionNumber, props.state.data, Pair(theme.onBackgroundSecondary, theme.onBackground))
        }
    }
}

private fun RBuilder.YourAnswer(answer: String) {
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            gap = Gap("16px")
        }
        p {
            +"YOUR ANSWER"
        }
        h2 {
            +answer
        }
    }

}

private fun RBuilder.CorrectAnswer(song: Track) {
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            gap = Gap("16px")
        }
        p {
            +"CORRECT ANSWER"
        }
        SongItem(song)
    }
}

private fun RBuilder.SongItem(song: Track) = child(songItem) {
    attrs { this.song = song }
}

external interface SongProps : RProps {
    var song: Track
}
val songItem = functionalComponent<SongProps> { props ->
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.row
            gap = Gap("32px")
        }

        styledImg(src = props.song.imageUrl) {
            css {
                marginTop = 12.px
                width = 96.px
                height = 96.px
            }
        }

        div {
            h2 {
                +props.song.name
            }
            styledH2 {
                css { color = theme.onBackgroundSecondary }
                +props.song.artists.joinToString { it.name }
            }
        }
    }
}