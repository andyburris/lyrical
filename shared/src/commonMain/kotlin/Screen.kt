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

sealed class State {
    sealed class Home : State() {
        object LoggedOut : Home()
        data class LoggedIn(val selectedPlaylists: List<SimplePlaylist>, val config: GameOptions, val addPlaylistState: AddPlaylistState) : Home() {
            data class AddPlaylistState(val searchTerm: String, val spotifySearchState: PlaylistSearchState, val myPlaylistSearchState: PlaylistSearchState)
        }
    }
    object Loading : State()
    sealed class GameState() : State() {
        abstract val game: Game
        data class Question(val questionNumber: Int, override val game: Game) : GameState() {
            val question: GameQuestion get() = game.questions[questionNumber]
            val lyric get() = question.lyric
            val nextLyric get() = question.nextLyric
            val artist get() = question.artist
            val playlist get() = question.playlist
        }
        data class Answer(val questionNumber: Int, override val game: Game) : GameState() {
            val track get() = game.questions[questionNumber].trackWithLyrics.sourcedTrack
            val answer: GameAnswer get() = game.questions[questionNumber].answer
        }
        data class End(override val game: Game) : GameState()
    }
}


sealed class PlaylistSearchState {
    data class Results(val playlists: List<Pair<SimplePlaylist, Boolean>>) : PlaylistSearchState()
    //object RequiresLogin : PlaylistSearchState()
    object Loading : PlaylistSearchState()
    object Error : PlaylistSearchState()
}