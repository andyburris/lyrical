import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader

fun main() {

    val playlistURI = "spotify:playlist:07DnxA8uWkBU10h3fubaqd"
    val tristanPlaylist = "spotify:playlist:5TjcPSTcEyuCkpyZrZpVZQ"
    val evanPlaylist = "spotify:playlist:7zLV1mjzuKaghELlJ0P1QG"
    val allOut10s = "spotify:playlist:37i9dQZF1DX5Ejj0EkURtP"

    val reader = System.`in`.bufferedReader()

    runBlocking {
        val machine = Machine(this)
        machine.screen.collect { state ->
            when(state) {
                State.Loading -> {
                    println("Loading...")
                }
                is State.Setup -> {
                    val searchState = when(val tabState = state.tabState) {
                        is State.Setup.TabState.SpotifyPlaylists -> tabState.searchState
                        is State.Setup.TabState.MyPlaylists -> tabState.searchState
                        is State.Setup.TabState.URL -> tabState.searchState
                    }
                    val featuredPlaylists = when(searchState) {
                        is PlaylistSearchState.Results -> searchState.playlists.map { "${it.name} (${it.uri.uri})" }.joinToString()
                        PlaylistSearchState.RequiresLogin -> "requires login"
                        PlaylistSearchState.Loading -> "loading"
                        PlaylistSearchState.Error -> "error"
                    }
                    println("Featured Playlists: $featuredPlaylists")
                    if (searchState is PlaylistSearchState.Results) {
                        println("How many songs?")
                        val amountOfSongs = reader.readUntil { it.toIntOrNull() != null }.toInt()
                        println("What playlist urls? (separate with commas)")
                        val urls = reader.readLine().split(",").map { it.trim() }
                        machine += Action.StartGame(urls.map { it.playlistURIFromURL() }, config = GameConfig(amountOfSongs))
                    }
                }
                is State.GameState.Question -> {
                    println("Lyric: ${state.lyric}")
                    var potentialPoints = 1.0
                    reader.readUntil {
                        when(it) {
                            ">artist" -> {
                                println("- ${state.question.trackWithLyrics.track.artists.first().name}")
                                potentialPoints -= 0.5
                                false
                            }
                            ">nextline" -> {
                                println("Line 2: ${state.nextLyric}")
                                potentialPoints -= 0.25
                                false
                            }
                            else -> {
                                val screen = state.toScreen().answer(it, potentialPoints)
                                machine += Action.OpenScreen(screen)
                                true
                            }
                        }
                    }
                }
                is State.GameState.Answer -> {
                    when(val answer = state.answer) {
                        is GameAnswer.Correct -> println("Correct! The song was ${state.track.name} by ${state.track.artists.joinToString { it.name }}. Press enter to continue")
                        is GameAnswer.Incorrect -> println("Incorrect. You guessed ${answer.answer}, but the song was ${state.track.name} - ${state.track.artists.joinToString { it.name }}. Press enter to continue")
                        is GameAnswer.Skipped -> println("Skipped. The song was ${state.track.name} - ${state.track.artists.joinToString { it.name }}. Press enter to continue\"")
                    }
                    reader.readUntilEnter()
                    val screen = state.toScreen()
                    machine += Action.OpenScreen(if (screen.isLastAnswer) screen.end() else screen.nextQuestion())
                }
                is State.GameState.End -> {
                    println("That's game! You scored ${state.data.points} out of ${state.data.questions.size} possible points")
                }
            }
        }
        machine += Action.OpenScreen(Screen.Setup())
    }
}


private fun BufferedReader.readUntil(onRead: (String) -> Boolean): String {
    var flag: String? = null
    while (flag == null) {
        val line = readLine()
        if (onRead(line)) flag = line
    }
    return flag
}

private fun BufferedReader.readUntilNotBlank(onRead: (input: String) -> Unit) {
    var flag = ""
    while (flag.isBlank()) {
        flag = readLine()
    }
    onRead.invoke(flag)
}

private fun BufferedReader.readUntilEnter() {
    readLine()
}