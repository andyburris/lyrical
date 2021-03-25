package ui.game

import Game
import com.adamratzman.spotify.models.SimplePlaylist
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.button.ButtonGroup
import com.github.mpetuska.khakra.button.IconButton
import com.github.mpetuska.khakra.image.Image
import com.github.mpetuska.khakra.input.Input
import com.github.mpetuska.khakra.kt.set
import com.github.mpetuska.khakra.layout.*
import com.github.mpetuska.khakra.tooltip.Tooltip
import flexColumn
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.js.*
import react.*
import size
import styled.*
import targetInputValue
import ui.common.Icon
import ui.common.Chip
import ui.khakra.*
import ui.theme

fun RBuilder.QuestionScreen(data: State.GameState.Question, onAnswer: (GameAction.AnswerQuestion) -> Unit) =
    child(question) {
        attrs {
            this.state = data
            this.onAnswer = onAnswer
        }
    }

external interface QuestionProps : RProps {
    var state: State.GameState.Question
    var onAnswer: (GameAction.AnswerQuestion) -> Unit
}

val question = functionalComponent<QuestionProps> { props ->
    val (answer, setAnswer) = useState("")
    val (showingNextLine, setShowingNextLine) = useState(false)
    val (showingArtist, setShowingArtist) = useState(false)
    Box() {
        VStack({ spacing = 32; align = "stretch"; maxW = "1280px"; p = arrayOf("32", "48", "64"); minH = "100vh"; justifyContent = "center"; margin="auto" }) {
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
                HStack({ spacing = arrayOf(8, 12, 16); justifyContent = "end" }) {
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
                            val action = GameAction.AnswerQuestion(UserAnswer.Answer(answer, showingNextLine, showingArtist))
                            props.onAnswer(action)
                        }
                    }
                })
            }
            ButtonGroup({
                isAttached = true
                size = "fab"
                position = arrayOf("fixed", "static")
                bottom = 32
                right = 32
            }) {
                Button({
                    onClick = {
                        val action = GameAction.AnswerQuestion(UserAnswer.Answer(answer, showingNextLine, showingArtist))
                        props.onAnswer(action)
                    }
                    position = "static"
                }) { +"Answer" }
                IconButton({
                    isRound = true
                    width = arrayOf(48, 64)
                    bg = "brandDark.primaryDark"
                    onClick = {
                        val action = GameAction.AnswerQuestion(UserAnswer.Skipped(showingNextLine, showingArtist))
                        props.onAnswer(action)
                    }
                    position = "static"
                }) { Icon(Icon.Skip) }
            }
        }
    }
}



fun RBuilder.Header(state: State.GameState.Question, onLeaveGame: () -> Unit) {
    HStack({ spacing = 16; width = "100%" }) {
        HStack({ spacing = arrayOf(12, 16); width = "100%" }) {
            Icon(Icon.Clear, alpha = 0.5) {
                onClick = onLeaveGame
            }
            VStack({ spacing = 0; width = "100%"; alignItems = "start" }) {
                SectionHeader { +"Question ${state.questionNumber + 1}/${state.game.questions.size}" }
                if (state.game.config.showSourcePlaylist) {
                    Subtitle2 ({ width = "100%" }) {
                        Subtitle2({ `as` = "span"; textColor = colorTheme() + "onBackgroundTernary" }) { +"from" }
                        Subtitle2({ `as` = "span"; textColor = colorTheme() + "onBackgroundSecondary" }) { +" ${state.playlist.name}" }
                    }
                }
            }
        }
        Subtitle1 ({ flexShrink = 0 }) {
            Subtitle1({ `as` = "span"; textColor = colorTheme() + "onBackgroundSecondary" }) { +state.game.points.toString() }
            Subtitle1({ `as` = "span"; textColor = colorTheme() + "onBackgroundTernary" }) { +"/${state.game.questions.size} pts" }
        }
    }
}