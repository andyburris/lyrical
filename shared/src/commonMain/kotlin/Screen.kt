import com.adamratzman.spotify.models.SimplePlaylist

/*sealed class Screen {
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
                val currentSong = data.questions[questionNumber].trackWithLyrics.sourcedTrack
                val gameAnswer = when(answer.formatAnswer() == currentSong.track.name.formatAnswer()) {
                    true -> GameAnswer.Answered.Correct(potentialPoints)
                    false -> GameAnswer.Answered.Incorrect(answer)
                }
                val updatedGame = data.copy(questions = data.questions.replace(questionNumber) { it.copy(answer = gameAnswer)})
                return Answer(questionNumber, updatedGame)
            }
            fun skip() = Answer(questionNumber, data.copy(questions = data.questions.replace(questionNumber) { it.copy(answer = GameAnswer.Answered.Skipped)}))
        }
        data class Answer(val questionNumber: Int, override val data: Game) : GameScreen() {
            val isLastAnswer get() = questionNumber == data.questions.size - 1
            fun nextQuestion() = Question(questionNumber + 1, data)
            fun end() = End(data)
        }
        data class End(override val data: Game) : GameScreen()
    }
}*/

sealed class State {
    data class Setup(val selectedPlaylists: List<SimplePlaylist>, val config: GameConfig, val addPlaylistState: AddPlaylistState) : State() {
        data class AddPlaylistState(val searchTerm: String, val spotifySearchState: PlaylistSearchState, val myPlaylistSearchState: PlaylistSearchState)
        //override fun toScreen(): Screen.Setup = Screen.Setup(this.selectedPlaylists, this.config, this.tabState.toTab())
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
            //override fun toScreen(): Screen.GameScreen.Question = Screen.GameScreen.Question(data.questions.indexOf(question), data)
        }
        data class Answer(val questionNumber: Int, override val game: Game) : GameState() {
            val track get() = game.questions[questionNumber].trackWithLyrics.sourcedTrack
            val answer: GameAnswer get() = game.questions[questionNumber].answer
            //override fun toScreen(): Screen.GameScreen.Answer = Screen.GameScreen.Answer(questionNumber, data)
        }
        data class End(override val game: Game) : GameState()
    }

/*    open fun toScreen(): Screen = when(this) {
        is Setup -> this.toScreen()
        Loading -> Screen.Loading
        is GameState.Question -> this.toScreen()
        is GameState.Answer -> this.toScreen()
        is GameState.End -> Screen.GameScreen.End(data)
    }*/
}


sealed class PlaylistSearchState {
    data class Results(val playlists: List<Pair<SimplePlaylist, Boolean>>) : PlaylistSearchState()
    //object RequiresLogin : PlaylistSearchState()
    object Loading : PlaylistSearchState()
    object Error : PlaylistSearchState()
}