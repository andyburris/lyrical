package client

import GameAnswer
import Hint
import UserAnswer
import kotlinx.serialization.Serializable
import model.GenericGameTrack
import model.GenericPlaylist
import points

@Serializable
sealed class ClientGameQuestion {
    @Serializable object NotReached : ClientGameQuestion()
    @Serializable data class Unanswered(
        val lyric: String,
        val hints: Hints,
        val sourcePlaylist: SourcePlaylist,
    ) : ClientGameQuestion()
    @Serializable data class Answered(
        val track: GenericGameTrack,
        val startingLineIndex: Int,
        val answer: GameAnswer.Answered,
    ) : ClientGameQuestion()
}

@Serializable
sealed class SourcePlaylist {
    @Serializable object NotShown : SourcePlaylist()
    @Serializable data class Shown(val playlist: GenericPlaylist) : SourcePlaylist()
}

@Serializable
sealed class Hints {
    @Serializable object None : Hints()
    @Serializable data class NextLyric(val lyric: String) : Hints()
    @Serializable data class Artist(val name: String) : Hints()
    @Serializable data class Both(val nextLyric: String, val artistName: String) : Hints()
}

val Hints.nextLyric: String? get() = when(this) {
    Hints.None, is Hints.Artist -> null
    is Hints.NextLyric -> this.lyric
    is Hints.Both -> this.nextLyric
}

val Hints.artist: String? get() = when(this) {
    Hints.None, is Hints.NextLyric -> null
    is Hints.Artist -> this.name
    is Hints.Both -> this.artistName
}

fun ClientGameQuestion.Unanswered.withArtist(artistName: String): ClientGameQuestion.Unanswered = this.copy(hints = hints.withArtist(artistName))
fun ClientGameQuestion.Unanswered.withNextLyric(lyric: String): ClientGameQuestion.Unanswered = this.copy(hints = hints.withNextLyric(lyric))

fun List<ClientGameQuestion>.totalPoints() = this.sumOf { if (it is ClientGameQuestion.Answered) it.answer.points else 0.0 }

fun Hints.withArtist(artistName: String) = when(this) {
    Hints.None -> Hints.Artist(artistName)
    is Hints.NextLyric -> Hints.Both(this.lyric, artistName)
    is Hints.Artist -> this.copy(name = artistName)
    is Hints.Both -> this.copy(artistName = artistName)
}

fun Hints.withNextLyric(lyric: String) = when(this) {
    Hints.None -> Hints.NextLyric(lyric)
    is Hints.NextLyric -> this.copy(lyric = lyric)
    is Hints.Artist -> Hints.Both(nextLyric = lyric, artistName = this.name)
    is Hints.Both -> this.copy(nextLyric = lyric)
}