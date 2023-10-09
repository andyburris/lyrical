import com.adamratzman.spotify.models.SimplePlaylist
import kotlinx.serialization.Serializable

sealed class Screen {
    sealed class Home : Screen() {
        data object LoggedOut : Home()
        data class LoggedIn(
            val spotifyRepository: SpotifyRepository.LoggedIn,
            val setupState: SetupState,
        ) : Home()
    }
    sealed class GameScreen : Screen() {
        data class Loading(val loadingState: LoadingState) : GameScreen()
        data class Question(val questionNumber: Int, val game: Game) : GameScreen() {
            val question: GameQuestion get() = game.questions[questionNumber]
            val lyric get() = question.lyric
            val nextLyric get() = question.nextLyric
            val artist get() = question.artist
            val playlist get() = question.playlist
        }
        data class Answer(val questionNumber: Int, val game: Game) : GameScreen() {
            val track get() = game.questions[questionNumber].trackWithLyrics.sourcedTrack
            val answer: GameAnswer get() = game.questions[questionNumber].answer
        }
        data class End(val game: Game) : GameScreen()
    }
}

@Serializable data class SetupState(val selectedPlaylists: List<SimplePlaylist>, val options: GameOptions)


sealed class PlaylistSearchState {
    data class Results(val playlists: List<Pair<SimplePlaylist, Boolean>>) : PlaylistSearchState()
    //object RequiresLogin : PlaylistSearchState()
    object Loading : PlaylistSearchState()
    object Error : PlaylistSearchState()
}

sealed class LoadingState {
    data object ErrorLoading : LoadingState()
    data object LoadingSongs : LoadingState()
    data object LoadingLyrics : LoadingState()
}