package ui.game

import GameQuestion
import Screen
import flexColumn
import flexRow
import flexbox
import imageUrl
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.child
import react.dom.h1
import react.dom.h2
import react.dom.p
import react.functionalComponent
import size
import styled.*
import ui.common.Icon
import ui.theme

fun RBuilder.EndScreen(state: State.GameState.End, onRestart: () -> Unit) = child(endScreen) {
    attrs {
        this.state = state
        this.onRestart = onRestart
    }
}

external interface EndScreenProps : RProps {
    var state: State.GameState.End
    var onRestart: () -> Unit
}
val endScreen = functionalComponent<EndScreenProps> { props ->
    Screen(backgroundColor = theme.primary) {
        css {
            gap = Gap("64px")
        }
        Header(props.state, props.onRestart)
        SongList(props.state)
    }
}

private fun RBuilder.Header(gameState: State.GameState, onRestart: () -> Unit) {
    flexRow(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        flexColumn {
            styledP {
                +"YOUR SCORE"
            }
            h1 {
                val score = "${gameState.data.points}/${gameState.data.questions.size}"
                +score
                styledSpan {
                    css { color = theme.onPrimarySecondary }
                    +" pts"
                }
            }
        }
        styledButton {
            css { backgroundColor = theme.background }
            +"PLAY AGAIN"
            attrs {
                onClickFunction = {
                    onRestart.invoke()
                }
            }
        }
    }
}

private fun RBuilder.SongList(gameState: State.GameState) {
    flexColumn(gap = 32.px) {
        p { +"ANSWERS" }
        gameState.data.questions.forEachIndexed { index, gameQuestion ->
            QuestionItem(gameQuestion, index)
        }
    }
}

private fun RBuilder.QuestionItem(question: GameQuestion, index: Int) {
    flexRow(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        css { width = 100.pct }
        flexRow(alignItems = Align.center, gap = 32.px) {
            flexRow(justifyContent = JustifyContent.flexEnd, alignItems = Align.center, gap = 32.px) {
                css { size(64.px) }
                p { +"${index + 1}" }
                styledImg(src = question.trackWithLyrics.sourcedTrack.track.imageUrl) {
                    css { size(64.px) }
                }
            }
            h2 {
                +question.trackWithLyrics.sourcedTrack.track.name
            }
        }
        flexRow(alignItems = Align.center, gap = 16.px) {
            when(val answer = question.answer) {
                is GameAnswer.Correct -> flexColumn(gap = 8.px) {
                    if (answer.withArtist) { Icon(Icon.Profile) }
                    if (answer.withNextLine) { Icon(Icon.NextLine) }
                }
                is GameAnswer.Incorrect -> flexColumn {
                    styledP {
                        css { color = theme.onPrimarySecondary }
                        +"You Said"
                    }
                    styledP {
                        css { color = theme.onPrimary }
                        +answer.answer
                    }
                }
            }
            flexbox(justifyContent = JustifyContent.center, alignItems = Align.center) {
                css {
                    size(64.px)
                    borderRadius = 32.px
                    backgroundColor = if (question.answer is GameAnswer.Correct) theme.onPrimaryOverlay else theme.background
                    color = if (question.answer is GameAnswer.Correct) theme.onPrimary else theme.onBackground
                }
                val icon = when(question.answer) {
                    is GameAnswer.Correct -> Icon.Check
                    is GameAnswer.Incorrect -> Icon.Clear
                    GameAnswer.Skipped -> Icon.Skip
                    GameAnswer.Unanswered -> throw Error("The end screen shouldn't have any unanswered questions")
                }
                Icon(icon)
            }
        }
    }
}