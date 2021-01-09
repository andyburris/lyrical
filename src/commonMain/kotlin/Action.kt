sealed class Action {
    data class OpenScreen(val screen: Screen) : Action()
    data class UpdateScreen(val updatedScreen: Screen) : Action()
    data class StartGame(val playlistURIs: List<String>, val config: GameConfig = GameConfig()) : Action()
}