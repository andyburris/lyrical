package ui.game

import Game
import Screen
import UserAnswer
import com.adamratzman.spotify.models.SimplePlaylist
import flexColumn
import flexbox
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.js.*
import react.*
import react.dom.button
import react.dom.div
import react.dom.p
import size
import styled.*
import targetInputValue
import ui.common.Icon
import ui.theme

fun RBuilder.QuestionScreen(data: State.GameState.Question, onAnswer: (GameAction.AnswerQuestion) -> Unit) =
    child(question) {
        attrs {
            this.screen = data
            this.onAnswer = onAnswer
        }
    }

external interface QuestionProps : RProps {
    var screen: State.GameState.Question
    var onAnswer: (GameAction.AnswerQuestion) -> Unit
}

val question = functionalComponent<QuestionProps> { props ->
    val (answer, setAnswer) = useState("")
    val (showingNextLine, setShowingNextLine) = useState(false)
    val (showingArtist, setShowingArtist) = useState(false)
    Screen {
        css { gap = Gap("96px") }
        flexbox(direction = FlexDirection.column, gap = 32.px) {
            if (props.screen.data.config.showSourcePlaylist) {
                PlaylistSource(props.screen.playlist)
            }
            styledDiv {
                css {
                    width = 100.pct
                }
                styledP {
                    css {
                        color = theme.onBackgroundSecondary
                    }
                    +"Lyric".toUpperCase()
                }
                styledH1 {
                    val shownLyrics = props.screen.lyric + if (showingNextLine) " / " + props.screen.nextLyric else ""
                    +"“$shownLyrics”"
                }
                if (showingArtist) {
                    styledH1 {
                        css {
                            color = theme.onBackgroundSecondary
                        }
                        +"- ${props.screen.artist}"
                    }
                }
                flexbox(justifyContent = JustifyContent.flexEnd, gap = 8.px) {
                    css { marginTop = 8.px }
                    if (!showingNextLine) {
                        HoverTooltip("-0.25 points") {
                            chip("+ Next Line") { setShowingNextLine(true) }
                        }
                    }
                    if (!showingArtist) {
                        HoverTooltip("-0.5 points") {
                            chip("+ Show Artist") { setShowingArtist(true) }
                        }
                    }
                }
                styledP {
                    css {
                        color = theme.onBackgroundSecondary
                    }
                    +"Answer".toUpperCase()
                }
                styledInput(InputType.text) {
                    css {
                        backgroundColor = Color.transparent
                        border = "none"
                        color = theme.onBackground
                        fontFamily = "YoungSerif"
                        fontSize = 64.px
                        width = 100.pct
                    }
                    attrs {
                        placeholder = "Song Name"
                        onChangeFunction = {
                            val value = it.targetInputValue
                            setAnswer(value)
                        }
                        onKeyUpFunction = {
                            console.log("keyup")
                            it.preventDefault()
                            if (it.asDynamic().key == "Enter" && answer.isNotBlank()) {
                                console.log("hit")
                                props.onAnswer.invoke(GameAction.AnswerQuestion(UserAnswer.Answer(answer, showingNextLine, showingArtist)))
                            }
                        }
                    }
                }
            }
        }
        styledDiv {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                justifyContent = JustifyContent.center
                gap = Gap("16px")
            }
            HoverTooltip("-1 Points") {
                styledButton {
                    css {
                        padding(16.px)
                        display = Display.flex
                        justifyContent = JustifyContent.center
                        alignItems = Align.center
                        backgroundColor = theme.onBackgroundSecondary
                        size(64.px)
                    }
                    attrs {
                        onClickFunction = {
                            props.onAnswer.invoke(GameAction.AnswerQuestion(UserAnswer.Skipped))
                        }
                    }
                    Icon(Icon.Skip)
                }
            }

            button {
                +"Answer".toUpperCase()
                attrs {
                    onClickFunction = {
                        props.onAnswer.invoke(GameAction.AnswerQuestion(UserAnswer.Answer(answer, showingNextLine, showingArtist)))
                    }
                }
            }
            GameState(props.screen.questionNumber, props.screen.data, Pair(theme.primary, theme.onBackgroundSecondary))
        }
    }
}

fun RBuilder.chip(text: String, icon: Icon? = null, onClick: (() -> Unit)? = null) = child(chip) {
    attrs {
        this.text = text
        this.icon = icon
        this.onClick = onClick
    }
}

external interface ChipProps : RProps {
    var icon: Icon?
    var text: String
    var onClick: (() -> Unit)?
}

val chip = functionalComponent<ChipProps> { props ->
    styledDiv {
        css {
            backgroundColor = theme.onBackgroundPlaceholder
            borderRadius = 32.px
            padding(vertical = 4.px, horizontal = 12.px)
            color = theme.onBackground
            if (props.onClick != null) {
                cursor = Cursor.pointer
            }
        }
        attrs {
            if (props.onClick != null) {
                onClickFunction = {
                    props.onClick?.invoke()
                }
            }
        }
        p {
            +props.text
        }
    }
}

fun RBuilder.icon(icon: Icon) {
    when (icon) {
        Icon.Add -> TODO()
        Icon.Skip -> TODO()
    }
}

fun RBuilder.GameState(questionNumber: Int, game: Game, colors: Pair<Color, Color>) {
    div {
        styledP {
            css { color = colors.first }
            +"${questionNumber + 1}/${game.questions.size}"
        }
        styledP {
            css { color = colors.second }
            +"${+game.points} pts"
        }
    }
}

fun RBuilder.PlaylistSource(playlist: SimplePlaylist) {
    flexbox(gap = 16.px, justifyContent = JustifyContent.start, alignItems = Align.center) {
        styledImg(src = playlist.images.firstOrNull()?.url ?: "/assets/PlaylistPlaceholder.svg") {
            css { size(32.px) }
        }
        p {
            styledSpan {
                css { color = theme.onBackgroundPlaceholder }
                + "from "
            }
            styledSpan {
                css { color = theme.onBackground }
                + playlist.name
            }
        }
    }
}

private fun RBuilder.HoverTooltip(text: String, content: StyledDOMBuilder<DIV>.() -> Unit) = child(hoverTooltip) {
    attrs {
        this.text = text
        this.content = content
    }
}
interface HoverProps : RProps {
    var text: String
    var content: StyledDOMBuilder<DIV>.() -> Unit
}
private val hoverTooltip = functionalComponent<HoverProps> { props ->
    flexColumn(alignItems = Align.center) {
        props.content.invoke(this)
        val (hovering, setHovering) = useState(false)
        if (hovering) {
            flexColumn(alignItems = Align.center) {
                css {
                    size(0.px)
                }
                styledP {
                    css {
                        backgroundColor = theme.backgroundCard
                        marginTop = 8.px
                        padding(8.px)
                        borderRadius = 8.px
                        width = LinearDimension.maxContent
                    }
                    +props.text
                }
            }
        }
        attrs {
            onMouseOverFunction = { setHovering(true) }
            onMouseOutFunction = { setHovering(false) }
        }
    }
}