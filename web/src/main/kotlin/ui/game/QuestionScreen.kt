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
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.js.*
import react.*
import react.dom.div
import size
import styled.*
import targetInputValue
import ui.common.Icon
import ui.common.Chip
import ui.khakra.Heading1
import ui.khakra.SectionHeader
import ui.khakra.onChange
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
    Box({ backgroundColor = "brand.background" }) {
        Container({maxW = "1280px"}) {
            VStack({spacing = 4; align="stretch"}) {
                if (props.screen.data.config.showSourcePlaylist) {
                    PlaylistSource(props.screen.playlist)
                }
                VStack({spacing = 1; align="stretch"}) {
                    VStack({align="stretch"}) {
                        SectionHeader { +"LYRIC" }
                        Heading1 {
                            val shownLyrics = props.screen.lyric + if (showingNextLine) " / " + props.screen.nextLyric else ""
                            +"“$shownLyrics”"
                        }
                    }
                    HStack({spacing = 1}) {
                        if (!showingNextLine) {
                            Tooltip({label = "-0.25 points"}) {
                                Chip("+ Next Line") { setShowingNextLine(true) }
                            }
                        }
                        if (!showingArtist) {
                            Tooltip({label = "-0.5 points"}) {
                                Chip("+ Show Artist") { setShowingArtist(true) }
                            }
                        }
                    }
                }
                VStack({align="stretch"}) {
                    SectionHeader { +"LYRIC" }
                    Input({
                        variant="unstyled"
                        textStyle="heading"
                        this["placeholder"] = "Song Name"
                        onChange = {
                            setAnswer(it.targetInputValue)
                        }
                    })
                }

                HStack {
                    ButtonGroup({isAttached=true}) {
                        Button { +"Answer" }
                        IconButton { Icon(Icon.Skip) }
                    }
                    VStack({align="stretch"}) {
                        Text { +"Question ${props.screen.questionNumber + 1}/${props.screen.data.questions.size}" }
                        Text { +"${props.screen.data.points + 1}/${props.screen.data.questions.size}" }
                    }
                }
            }
        }
    }
}



fun RBuilder.GameState(questionNumber: Int, game: Game, colors: Pair<Color, Color>) {
    div {
        styledP {
            css { color = colors.first }
            +"Q${questionNumber + 1}/${game.questions.size}"
        }
        styledP {
            css { color = colors.second }
            +"${+game.points} pts"
        }
    }
}

fun RBuilder.PlaylistSource(playlist: SimplePlaylist) {
    HStack({spacing = 4}) {
        Image({boxSize=8; src=playlist.images.firstOrNull()?.url ?: "/assets/PlaylistPlaceholder.svg"})
        Text {
            styledSpan {
                css { color = theme.onBackgroundTernary }
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