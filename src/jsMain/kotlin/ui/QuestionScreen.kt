package ui

import Game
import Screen
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.*
import react.*
import react.dom.button
import react.dom.div
import react.dom.p
import styled.*
import targetInputValue
import ui.common.Icon

fun RBuilder.QuestionScreen(data: State.GameState.Question, onAnswer: (Screen.GameScreen.Answer) -> Unit) = child(question) {
    attrs {
        this.screen = data
        this.onAnswer = onAnswer
    }
}
external interface QuestionProps : RProps {
    var screen: State.GameState.Question
    var onAnswer: (Screen.GameScreen.Answer) -> Unit
}
val question = functionalComponent<QuestionProps> { props ->
    val (answer, setAnswer) = useState("")
    val (showingNextLine, setShowingNextLine) = useState(false)
    val (showingArtist, setShowingArtist) = useState(false)
    styledDiv {
        css {
            width = 100.vw
            height = 100.vh
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = Align.center
            padding(vertical = 128.px, horizontal = 196.px)
            boxSizing = BoxSizing.borderBox
            gap = Gap("64px")
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
                styledDiv {
                    css {
                        display = Display.flex
                        flexDirection = FlexDirection.row
                        justifyContent = JustifyContent.end
                        marginTop = 16.px
                        gap = Gap("8px")
                    }
                    if (!showingNextLine) {
                        chip("+ Next Line") { setShowingNextLine(true) }
                    }
                    if (!showingArtist) {
                        chip("+ Show Artist") { setShowingArtist(true) }
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
                        fontWeight = FontWeight.w700
                        fontSize = 64.px
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
                                val points = 1.0 - (if (showingArtist) 0.5 else 0.0) - (if (showingNextLine) 0.25 else 0.0)
                                props.onAnswer.invoke(props.screen.toScreen().answer(answer, points))
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
                styledButton {
                    val (hovering, setHovering) = useState(false)
                    css {
                        padding(16.px)
                        display = Display.flex
                        justifyContent = JustifyContent.center
                        alignItems = Align.center
                        backgroundColor = theme.onBackgroundSecondary
                    }
                    attrs {
                        onMouseOverFunction = { setHovering(true) }
                        onMouseOutFunction = { setHovering(false) }
                    }
                    styledImg(src = "/assets/icons/Skip.svg") {
                        attrs {
                            onClickFunction = {
                                props.onAnswer.invoke(props.screen.toScreen().skip())
                            }
                        }
                    }
                    if (hovering) {
                        p {
                            +"-1 Points"
                        }
                    }
                }
                button {
                    +"Answer".toUpperCase()
                    attrs {
                        onClickFunction = {
                            val points = 1.0 - (if (showingArtist) 0.5 else 0.0) - (if (showingNextLine) 0.25 else 0.0)
                            props.onAnswer.invoke(props.screen.toScreen().answer(answer, points))
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
            if(props.onClick != null) {
                cursor = Cursor.pointer
            }
        }
        attrs {
            if(props.onClick != null) {
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
    when(icon) {
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
            +"${+game.points } pts"
        }
    }
}