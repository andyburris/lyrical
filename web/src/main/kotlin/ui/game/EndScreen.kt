package ui.game

import GameQuestion
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.hooks.useDisclosure
import com.github.mpetuska.khakra.image.Image
import com.github.mpetuska.khakra.layout.*
import com.github.mpetuska.khakra.transition.Collapse
import flexColumn
import flexRow
import imageUrl
import isRight
import kotlinx.css.*
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import ui.common.Icon
import ui.khakra.*

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
    Box({ backgroundColor = colorTheme() + "primary" }) {
        VStack({ spacing = arrayOf("32", "48", "64"); align = "stretch"; maxW = "1280px"; p = arrayOf("32", "48", "64"); minH = "100vh"; justifyContent = "center"; margin="auto" }) {
            Header(props.state, props.onRestart)
            SongList(props.state)
        }
    }
}

private fun RBuilder.Header(gameState: State.GameState, onRestart: () -> Unit) {
    flexRow(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        flexColumn {
            SectionHeader {
                +"YOUR SCORE"
            }
            HStack({spacing = 0}) {
                Heading1 {
                    val score = "${gameState.game.points}/${gameState.game.questions.size}"
                    +score
                }
                Heading1({ whiteSpace = "pre" }, textColor = "onBackgroundSecondary") {
                    +" pts"
                }
            }

        }
        Button({
            size = "fab"
            backgroundColor = colorTheme() + "background"
            onClick = {
                onRestart.invoke()
            }
        }) {
            +"PLAY AGAIN"
        }
    }
}

private fun RBuilder.SongList(gameState: State.GameState) {
    flexColumn(gap = 32.px) {
        SectionHeader { +"ANSWERS" }
        gameState.game.questions.forEachIndexed { index, gameQuestion ->
            QuestionItem(gameQuestion, index)
        }
    }
}

private fun RBuilder.QuestionItem(question: GameQuestion, index: Int) {
    val disclosure = useDisclosure()
    VStack({ spacing = 0; width = "100%"; alignItems = "stretch" }) {
        HStack({
            w = "100%"
            spacing = arrayOf("16", "24", "32")
            onClick = { disclosure.onToggle() }
        }) {
            HStack({
                spacing = arrayOf("16", "24", "32")
                boxSize = arrayOf("32", "40", "48")
                justifyContent = "end"
            }) {
                SectionHeader { +"${index + 1}" }
                Image({
                    src = question.trackWithLyrics.sourcedTrack.track.imageUrl
                    boxSize = arrayOf("32", "40", "48")
                    flexShrink = 0
                })
            }
            Heading2({flexGrow = 1}) {
                +question.trackWithLyrics.sourcedTrack.track.name
            }
            Subtitle1({
                color = if (question.answer.isRight) colorTheme() + "onPrimary" else colorTheme() + "onPrimarySecondary"
                textAlign = "end"
            }) {
                +when(val answer = question.answer) {
                    is GameAnswer.Answered.Correct -> "+${answer.points} pts"
                    is GameAnswer.Answered.Incorrect -> "Incorrect"
                    is GameAnswer.Answered.Skipped -> "Skipped"
                    GameAnswer.Unanswered -> throw Error("The end screen shouldn't have any unanswered questions")
                }
            }
        }
        Collapse({
            `in` = disclosure.isOpen
        }) {
            VStack({ spacing = "24"; paddingTop = "24"; width = "100%" }) {
                VStack( { spacing = "16"; width = "100%"; alignItems = "start" }) {
                    HStack({ spacing = "16" }) {
                        Icon(Icon.Quote)
                        Subtitle1 { +(question.lyric + if ((question.answer as GameAnswer.Answered).withNextLine) " / ${question.nextLyric}" else "") }
                    }
                    HStack({ spacing = "16" }) {
                        Icon(Icon.Person)
                        Subtitle1 { +question.trackWithLyrics.sourcedTrack.track.artists.joinToString { it.name } }
                    }
                }
                Divider({ color = colorTheme() + "onPrimaryTernary" })
                VStack( { spacing = "16"; width = "100%" }) {
                    if (question.answer is GameAnswer.Answered.Incorrect) {
                        VStack({ spacing = "0"; width = "100%"; alignItems = "start" }) {
                            SectionHeader { +"You Said" }
                            Subtitle1 { +(question.answer as GameAnswer.Answered.Incorrect).answer }
                        }
                    }
                    VStack({ spacing = "0"; width = "100%"; alignItems = "start" }) {
                        SectionHeader { +"Hints Used" }
                        Subtitle1 {
                            val hints = listOf("Next Line" to (question.answer as GameAnswer.Answered).withNextLine, "Show Artist" to (question.answer as GameAnswer.Answered).withArtist)
                            val answeredHints = hints.filter { it.second }
                            +when {
                                answeredHints.isNotEmpty() -> answeredHints.joinToString { it.first }
                                else -> "None"
                            }
                        }
                    }
                }
            }
        }
    }

    /*HStack({w = "100%"}) {
        HStack ({
            spacing = "32"
            boxSize = "64"
            justifyContent = "end"
        }) {
            SectionHeader { +"${index + 1}" }
            Image({
                src = question.trackWithLyrics.sourcedTrack.track.imageUrl
                boxSize = "64"
                flexShrink = 0
            })
        }
        Heading2 {
            +question.trackWithLyrics.sourcedTrack.track.name
        }
        flexRow(alignItems = Align.center, gap = 16.px) {
            when(val answer = question.answer) {
                is GameAnswer.Answered.Correct -> flexColumn(gap = 8.px) {
                    if (answer.withArtist) { Icon(Icon.Person) }
                    if (answer.withNextLine) { Icon(Icon.NextLine) }
                }
                is GameAnswer.Answered.Incorrect -> flexColumn {
                    SectionHeader { +"You Said" }
                    Subtitle1 { +answer.answer }
                }
            }
            Center({
                backgroundColor = if (question.answer is GameAnswer.Answered.Correct) colorTheme() + "onPrimaryTernary" else colorTheme() + "background"
                color = if (question.answer is GameAnswer.Answered.Correct) colorTheme() + "onPrimary" else colorTheme() + "onBackground"
                borderRadius = "full"
                boxSize = "64"
            }) {
                val icon = when(question.answer) {
                    is GameAnswer.Answered.Correct -> Icon.Check
                    is GameAnswer.Answered.Incorrect -> Icon.Clear
                    is GameAnswer.Answered.Skipped -> Icon.Skip
                    GameAnswer.Unanswered -> throw Error("The end screen shouldn't have any unanswered questions")
                }
                Icon(icon)
            }
        }
    }*/
}