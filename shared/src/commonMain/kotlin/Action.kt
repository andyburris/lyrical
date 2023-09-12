import com.adamratzman.spotify.models.SimplePlaylist

sealed class Action {
/*    data class OpenScreen(val screen: Screen) : Action()
    data class UpdateScreen(val updatedScreen: Screen) : Action()
    data class StartGame(val playlistURIs: List<String>, val config: GameConfig = GameConfig()) : Action()*/

}

sealed class GameAction : Action() {
    data class RequestHint(val hint: GameHint) : GameAction()
    data class AnswerQuestion(val answer: UserAnswer) : GameAction()
    data object NextQuestion : GameAction()
}

sealed class AuthAction : Action() {
    data class Authenticate(val state: String) : AuthAction()
    data class CheckAuthentication(val token: String, val type: String, val expiresIn: Int, val state: String) : AuthAction()
    data object LogOut : AuthAction()
}