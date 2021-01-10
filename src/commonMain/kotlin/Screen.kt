import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import kotlin.random.Random

sealed class Screen {
    data class Setup(val selectedPlaylists: List<SimplePlaylist> = emptyList(), val config: GameConfig = GameConfig(), val tab: Tab = Tab.SpotifyPlaylists("")) : Screen() {
        sealed class Tab {
            data class SpotifyPlaylists(val searchTerm: String) : Tab()
            data class MyPlaylists(val searchTerm: String) : Tab()
            data class URL(val searchURL: String) : Tab()
        }
    }
    object Loading : Screen()
    sealed class GameScreen() : Screen() {
        abstract val data: Game
        data class Question(val questionNumber: Int, override val data: Game) : GameScreen() {
            fun answer(answer: String, potentialPoints: Double): Answer {
                val currentSong = data.questions[questionNumber].trackWithLyrics.track
                val gameAnswer = when(answer.formatAnswer() == currentSong.name.formatAnswer()) {
                    true -> GameAnswer.Correct(potentialPoints)
                    false -> GameAnswer.Incorrect(answer)
                }
                val updatedGame = data.copy(questions = data.questions.replace(questionNumber) { it.copy(answer = gameAnswer)})
                return Answer(questionNumber, updatedGame)
            }
            fun skip() = Answer(questionNumber, data.copy(questions = data.questions.replace(questionNumber) { it.copy(answer = GameAnswer.Skipped)}))
        }
        data class Answer(val questionNumber: Int, override val data: Game) : GameScreen() {
            val isLastAnswer get() = questionNumber == data.questions.size - 1
            fun nextQuestion() = Question(questionNumber + 1, data)
            fun end() = End(data)
        }
        data class End(override val data: Game) : GameScreen()
    }
}

sealed class State {
    data class Setup(val selectedPlaylists: List<SimplePlaylist>, val config: GameConfig, val tabState: TabState) : State() {
        sealed class TabState {
            abstract fun toTab(): Screen.Setup.Tab
            data class SpotifyPlaylists(val searchTerm: String, val searchState: PlaylistSearchState) : TabState() {
                override fun toTab(): Screen.Setup.Tab.SpotifyPlaylists = Screen.Setup.Tab.SpotifyPlaylists(searchTerm)
            }
            data class MyPlaylists(val searchTerm: String, val searchState: PlaylistSearchState) : TabState() {
                override fun toTab(): Screen.Setup.Tab.MyPlaylists = Screen.Setup.Tab.MyPlaylists(searchTerm)
            }
            data class URL(val searchURL: String, val searchState: PlaylistSearchState) : TabState(){
                override fun toTab(): Screen.Setup.Tab.URL = Screen.Setup.Tab.URL(searchURL)
            }
        }

        override fun toScreen(): Screen.Setup = Screen.Setup(this.selectedPlaylists, this.config, this.tabState.toTab())
    }
    object Loading : State()
    sealed class GameState() : State() {
        abstract val data: Game
        data class Question(val questionNumber: Int, override val data: Game) : GameState() {
            val question: GameQuestion get() = data.questions[questionNumber]
            private val randomLine: Int = Random.nextInt(question.trackWithLyrics.lyrics.size - 1)
            val lyric get() = question.trackWithLyrics.lyrics[randomLine]
            val nextLyric get() = question.trackWithLyrics.lyrics[randomLine + 1]
            val artist get() = question.trackWithLyrics.track.artists.first().name
            override fun toScreen(): Screen.GameScreen.Question = Screen.GameScreen.Question(data.questions.indexOf(question), data)
        }
        data class Answer(val questionNumber: Int, override val data: Game) : GameState() {
            val track get() = data.questions[questionNumber].trackWithLyrics.track
            val answer: GameAnswer get() = data.questions[questionNumber].answer
            override fun toScreen(): Screen.GameScreen.Answer = Screen.GameScreen.Answer(questionNumber, data)
        }
        data class End(override val data: Game) : GameState()
    }

    open fun toScreen(): Screen = when(this) {
        is Setup -> this.toScreen()
        Loading -> Screen.Loading
        is GameState.Question -> this.toScreen()
        is GameState.Answer -> this.toScreen()
        is GameState.End -> Screen.GameScreen.End(data)
    }
}


sealed class PlaylistSearchState {
    data class Results(val playlists: List<Pair<SimplePlaylist, Boolean>>) : PlaylistSearchState()
    object RequiresLogin : PlaylistSearchState()
    object Loading : PlaylistSearchState()
    object Error : PlaylistSearchState()
}
