package ui.game

import client.Screen
import com.adamratzman.spotify.models.Track
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.image.Image
import com.github.mpetuska.khakra.layout.Box
import com.github.mpetuska.khakra.layout.HStack
import com.github.mpetuska.khakra.layout.VStack
import imageUrl
import kotlinx.browser.window
import react.*
import ui.ChakraTheme
import ui.khakra.*

fun RBuilder.AnswerScreen(state: Screen.GameScreen.Answer, onNextScreen: () -> Unit) = child(answerScreen) {
    attrs {
        this.state = state
        this.onNextScreen = onNextScreen
    }
}

external interface AnswerProps : RProps {
    var state: Screen.GameScreen.Answer
    var onNextScreen: () -> Unit
}

val answerScreen = functionalComponent<AnswerProps> { props ->
    useEffectWithCleanup {
        window.onkeypress = {
            when (it.key) {
                "Enter" -> props.onNextScreen.invoke()
            }
        }
        { window.onkeypress = null }
    }

    Box({ backgroundColor = colorTheme() + if (props.state.answer is GameAnswer.Answered.Correct) "primary" else "background" }) {
        VStack({ spacing = arrayOf(32, 48, 64); maxW = "1280px"; alignItems = "start"; p = arrayOf("32", "48", "64"); minH = "100vh"; justifyContent = "center"; margin="auto" }) {
            HStack({ spacing = arrayOf(16, 24, 32); width = "100%" }) {
                VStack({ spacing = 0; alignItems = "start"; width = "100%" }) {
                    Heading1(textColor = if (props.state.answer is GameAnswer.Answered.Correct) ChakraTheme.onPrimary else ChakraTheme.onBackground) {
                        when(props.state.answer) {
                            is GameAnswer.Answered.Correct -> +"Correct!"
                            is GameAnswer.Answered.Incorrect -> +"Incorrect"
                            is GameAnswer.Answered.Skipped -> +"Skipped"
                        }
                    }
                    SectionHeader(textColor = if (props.state.answer is GameAnswer.Answered.Correct) ChakraTheme.onPrimarySecondary else ChakraTheme.onBackgroundSecondary) { +"Question ${props.state.questionNumber + 1}/${props.state.game.questions.size}" }
                }
                Subtitle1 ({ flexShrink = 0 }) {
                    Subtitle1({ `as` = "span"; textColor = if (props.state.answer is GameAnswer.Answered.Correct) ChakraTheme.onPrimary else ChakraTheme.onBackground }) { +props.state.game.points.toString() }
                    Subtitle1({ `as` = "span"; textColor = if (props.state.answer is GameAnswer.Answered.Correct) ChakraTheme.onPrimarySecondary else ChakraTheme.onBackgroundSecondary }) { +"/${props.state.game.questions.size} pts" }
                }
            }


            VStack({
                padding = arrayOf(24, 32, 48)
                spacing = arrayOf(24, 32, 48)
                borderRadius = 16
                width = "100%"
                backgroundColor = colorTheme() + if (props.state.answer is GameAnswer.Answered.Correct) "primaryDark" else "backgroundDark"
                alignItems = "start"
            }) {
                when (val answer = props.state.answer) {
                    is GameAnswer.Answered.Correct -> {
                        SongItem(props.state.track.track, true)
                    }
                    is GameAnswer.Answered.Incorrect -> {
                        YourAnswer(answer.answer)
                        CorrectAnswer(props.state.track.track)
                    }
                    is GameAnswer.Answered.Skipped -> {
                        CorrectAnswer(props.state.track.track)
                    }
                }
            }

            Button({
                size = "fab"
                variant =  if (props.state.answer is GameAnswer.Answered.Correct) "solidBackground" else "solid"
                onClick = {
                    props.onNextScreen.invoke()
                }
            }) {
                when(props.state.questionNumber == props.state.game.questions.size - 1) {
                    false -> +"Next Question".toUpperCase()
                    true -> +"End Game".toUpperCase()
                }
            }
        }
    }
}

private fun RBuilder.YourAnswer(answer: String) {
    VStack({ spacing = arrayOf(8, 12, 16); alignItems = "start" }) {
        SectionHeader { +"YOUR ANSWER" }
        Heading2 { +answer }
    }
}

private fun RBuilder.CorrectAnswer(song: Track) {
    VStack({ spacing = arrayOf(8, 12, 16); alignItems = "start" }) {
        SectionHeader { +"CORRECT ANSWER" }
        SongItem(song, false)
    }
}

private fun RBuilder.SongItem(song: Track, correct: Boolean) = child(songItem) {
    attrs {
        this.song = song
        this.correct = correct
    }
}

external interface SongProps : RProps {
    var song: Track
    var correct: Boolean
}
val songItem = functionalComponent<SongProps> { props ->
    HStack({
        spacing = arrayOf(16, 24, 32)
        alignItems = "start"
    }) {
        Image({
            src = props.song.imageUrl
            mt = arrayOf(4, 2, 0)
            boxSize = arrayOf(48, 72, 96)
        })

        VStack({spacing = "0"; alignItems = "start"}) {
            Heading2(textColor = if (props.correct) ChakraTheme.onPrimary else ChakraTheme.onBackground) { +props.song.name }
            Heading2(textColor = if (props.correct) ChakraTheme.onPrimarySecondary else ChakraTheme.onBackgroundSecondary) { +props.song.artists.joinToString { it.name } }
        }
    }
}