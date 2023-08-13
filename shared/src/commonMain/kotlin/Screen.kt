import com.adamratzman.spotify.models.SimplePlaylist


sealed class State {
    sealed class Home : State() {
        object LoggedOut : Home()
        data class LoggedIn(val selectedPlaylists: List<SimplePlaylist>, val config: GameConfig, val addPlaylistState: AddPlaylistState) : Home() {
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