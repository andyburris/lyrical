import com.adamratzman.spotify.models.SimplePlaylist

sealed class Action {
/*    data class OpenScreen(val screen: Screen) : Action()
    data class UpdateScreen(val updatedScreen: Screen) : Action()
    data class StartGame(val playlistURIs: List<String>, val config: GameConfig = GameConfig()) : Action()*/
    data class Authenticate(val state: String) : Action()
    data class CheckAuthentication(val token: String, val type: String, val expiresIn: Int, val state: String) : Action()
}

sealed class GameAction : Action() {
    data class AnswerQuestion(val answer: UserAnswer) : GameAction()
    object NextQuestion : GameAction()
    object RestartGame : GameAction()
}

sealed class SetupAction : Action() {
    data class AddPlaylist(val playlist: SimplePlaylist) : SetupAction()
    data class RemovePlaylist(val playlist: SimplePlaylist) : SetupAction()
    data class UpdateConfig(val updatedConfig: GameConfig) : SetupAction()
    data class UpdateSearch(val searchTerm: String) : SetupAction()
    data class StartGame(val playlists: List<SimplePlaylist>, val config: GameConfig) : SetupAction()
}
