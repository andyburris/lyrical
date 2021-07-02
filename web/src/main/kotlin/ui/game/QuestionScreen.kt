package ui.game

import client.Screen
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.button.ButtonGroup
import com.github.mpetuska.khakra.button.IconButton
import com.github.mpetuska.khakra.input.Input
import com.github.mpetuska.khakra.kt.set
import com.github.mpetuska.khakra.layout.*
import com.github.mpetuska.khakra.tooltip.Tooltip
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.html.js.*
import react.*
import styled.*
import targetInputValue
import ui.ChakraTheme
import ui.common.Icon
import ui.common.Chip
import ui.khakra.*

fun RBuilder.QuestionScreen(data: Screen.GameScreen.Question, onAnswer: (GameAction.AnswerQuestion) -> Unit) =
    child(question) {
        attrs {
            this.state = data
            this.onAnswer = onAnswer
        }
    }

external interface QuestionProps : RProps {
    var state: Screen.GameScreen.Question
    var onAnswer: (GameAction.AnswerQuestion) -> Unit
}

val question = functionalComponent<QuestionProps> { props ->
    val (showingNextLine, setShowingNextLine) = useState(false)
    val (showingArtist, setShowingArtist) = useState(false)
    Box() {
        VStack({
            spacing = 32
            align = "stretch"
            maxW = "1280px"
            p = arrayOf("32", "48", "64")
            minH = "100vh"
            justifyContent = arrayOf("space-between", "center")
            margin = "auto"
        }) {
            VStack({
                spacing = 32
                align = "stretch"
            }) {
                Header(props.state) {
                    println("redirecting to ${window.location.host}")
                    val http = window.location.href.takeWhile { it != ':' }
                    window.location.href = "$http://${window.location.host}"
                }
                VStack({ spacing = 4; align = "stretch" }) {
                    VStack({ align = "stretch"; spacing = 0 }) {
                        SectionHeader { +"LYRIC" }
                        Heading1 {
                            val shownLyrics = props.state.lyric + if (showingNextLine) " / " + props.state.nextLyric else ""
                            +"“$shownLyrics”"
                        }
                        if (showingArtist) {
                            Heading1 { +"- ${props.state.artist}" }
                        }
                    }
                    HStack({ spacing = arrayOf(8, 12, 16); justifyContent = "flex-end"; pt = 8 }) {
                        if (!showingNextLine) {
                            Tooltip({ label = "-0.25 points" }) {
                                Chip("+ Next Line") { setShowingNextLine(true) }
                            }
                        }
                        if (!showingArtist) {
                            Tooltip({ label = "-0.5 points" }) {
                                Chip("+ Show Artist") { setShowingArtist(true) }
                            }
                        }
                    }
                }
            }
            Bottom { answer, skipped ->
                val action = when {
                    !skipped -> GameAction.AnswerQuestion(UserAnswer.Answer(answer, showingNextLine, showingArtist))
                    else -> GameAction.AnswerQuestion(UserAnswer.Skipped(showingNextLine, showingArtist))
                }
                props.onAnswer(action)
            }
        }
    }
}

private fun RBuilder.Bottom(onAnswer: (String, Boolean) -> Unit) {
    val (answer, setAnswer) = useState("")
    VStack({ spacing = 32; align = "flex-start"}) {
        VStack({ align = "stretch"; spacing = 0 }) {
            SectionHeader { +"ANSWER" }
            Input({
                variant = "unstyled"
                size = "unstyled"
                textStyle = "heading"
                this["autoFocus"] = true
                this["placeholder"] = "Song Name"
                onChange = {
                    setAnswer(it.targetInputValue)
                }
                onKeyUp = {
                    if (it.asDynamic().key == "Enter" && answer.isNotBlank()) {
                        onAnswer(answer, false)
                    }
                }
            })
        }
        ButtonGroup({
            isAttached = true
            size = "fabStatic"
            bottom = 32
            right = 32
        }) {
            Button({
                isDisabled = answer.isBlank()
                onClick = {
                    onAnswer(answer, false)
                }
            }) { +"Answer" }
            IconButton({
                isRound = true
                width = arrayOf(48, 64)
                bg = "brandDark.primaryDark"
                onClick = {
                    onAnswer(answer, true)
                }
            }) { Icon(Icon.Skip, color = ChakraTheme.onPrimary) }
        }
    }
}

fun RBuilder.Header(state: Screen.GameScreen.Question, onLeaveGame: () -> Unit) {
    HStack({ spacing = 16; width = "100%" }) {
        HStack({ spacing = arrayOf(12, 16); width = "100%" }) {
            Icon(Icon.Clear, color = ChakraTheme.onBackgroundSecondary) {
                onClick = onLeaveGame
            }
            VStack({ spacing = 0; width = "100%"; alignItems = "start" }) {
                SectionHeader { +"Question ${state.questionNumber + 1}/${state.game.questions.size}" }
                if (state.game.config.showSourcePlaylist) {
                    Subtitle2 ({ width = "100%" }) {
                        Subtitle2({ `as` = "span"; textColor = ChakraTheme.onBackgroundTernary }) { +"from" }
                        Subtitle2({ `as` = "span"; textColor = ChakraTheme.onBackgroundSecondary }) { +" ${state.playlist.name}" }
                    }
                }
            }
        }
        Subtitle1 ({ flexShrink = 0 }) {
            Subtitle1({ `as` = "span" }, textColor = ChakraTheme.onBackgroundSecondary) { +state.game.points.toString() }
            Subtitle1({ `as` = "span" }, textColor = ChakraTheme.onBackgroundTernary) { +"/${state.game.questions.size} pts" }
        }
    }
}