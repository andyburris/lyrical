package client

import Action
import model.GenericPlaylist

sealed class ClientAction

/*sealed class GameAction : ClientAction() {
    data class AnswerQuestion(val answer: UserAnswer) : GameAction()
    object NextQuestion : GameAction()
    object RestartGame : GameAction()
}*/

sealed class SetupAction : ClientAction() {
    data class AddPlaylist(val playlist: GenericPlaylist) : SetupAction()
    data class RemovePlaylist(val playlist: GenericPlaylist) : SetupAction()
    data class UpdateSearch(val searchTerm: String) : SetupAction()
    data class StartLobby(val playlists: List<GenericPlaylist>) : SetupAction()
}

sealed class AuthAction : ClientAction() {
    data class Authenticate(val state: String) : AuthAction()
    data class CheckAuthentication(val token: String, val type: String, val expiresIn: Int, val state: String) : AuthAction()
}