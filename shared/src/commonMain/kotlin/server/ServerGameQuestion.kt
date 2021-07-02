package server

import GameAnswer
import GameScreen
import UserAnswer
import client.ClientGameQuestion
import client.Hints
import client.SourcePlaylist
import formatAnswer
import kotlinx.serialization.Serializable
import model.GenericGameTrack

@Serializable
data class ServerGameQuestion(
    val trackWithLyrics: GenericGameTrack,
    val startingLineIndex: Int,
) {
    val lyric = trackWithLyrics.lyrics[startingLineIndex]
    val nextLyric = trackWithLyrics.lyrics.getOrNull(startingLineIndex + 1) ?: "[End of Song]"
    val artist get() = trackWithLyrics.track.artists.first().name
    val playlist get() = trackWithLyrics.sourcePlaylist
}

fun ServerGameQuestion.check(userAnswer: UserAnswer, serverUserAnswer: GameAnswer.Unanswered) = when(userAnswer) {
    is UserAnswer.Answer -> {
        println("answered, formatted user answer = ${userAnswer.answer.formatAnswer()}, formatted correct answer = ${this.trackWithLyrics.track.name.formatAnswer()}")
        when (userAnswer.answer.formatAnswer() == this.trackWithLyrics.track.name.formatAnswer()) {
            true -> GameAnswer.Answered.Correct(serverUserAnswer.hintsUsed)
            false -> GameAnswer.Answered.Incorrect(userAnswer.answer, serverUserAnswer.hintsUsed)
        }
    }
    is UserAnswer.Skipped -> GameAnswer.Answered.Skipped(serverUserAnswer.hintsUsed)
}

fun List<ServerGameQuestion>.toClientQuestions(currentGameScreen: GameScreen, userAnswers: List<GameAnswer>, showSourcePlaylist: Boolean): List<ClientGameQuestion> = this.mapIndexed { index, question ->
    when (val answer = userAnswers[index]){
        is GameAnswer.Unanswered -> when {
            currentGameScreen is GameScreen.Question && currentGameScreen.questionIndex == index -> question.toClientUnanswered(answer, showSourcePlaylist)
            else -> ClientGameQuestion.NotReached
        }
        is GameAnswer.Answered -> question.toClientAnswered(answer)
    }
}

fun ServerGameQuestion.toClientAnswered(userAnswer: GameAnswer.Answered) = ClientGameQuestion.Answered(trackWithLyrics, this.startingLineIndex, userAnswer)
fun ServerGameQuestion.toClientUnanswered(userAnswer: GameAnswer.Unanswered, showSourcePlaylist: Boolean) = ClientGameQuestion.Unanswered(
    lyric = this.lyric,
    hints = when {
        Hint.NextLyric in userAnswer.hintsUsed && Hint.Artist in userAnswer.hintsUsed -> Hints.Both(this.nextLyric, this.artist)
        Hint.NextLyric in userAnswer.hintsUsed -> Hints.NextLyric(this.nextLyric)
        Hint.Artist in userAnswer.hintsUsed -> Hints.Artist(this.artist)
        else -> Hints.None
    },
    sourcePlaylist = if (showSourcePlaylist) SourcePlaylist.Shown(this.playlist) else SourcePlaylist.NotShown
)