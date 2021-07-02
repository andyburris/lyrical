import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import model.GenericPlaylist
import server.ServerGameQuestion
import server.UserState

@Serializable
sealed class Action

@Serializable
sealed class UserAction : Action() {

    @Serializable sealed class Host : UserAction() {
        @Serializable object LoadGame : Host()
    }
    @Serializable object JoinRoom : UserAction()
    @Serializable object LeaveRoom : UserAction()
}


@Serializable
sealed class LobbyAction : UserAction() {

    @Serializable sealed class Host : LobbyAction() {
        @Serializable data class UpdateConfig(val updatedConfig: GameConfig) : Host()
        @Serializable data class KickUser(val kicking: User) : Host()
        @Serializable data class UpdatePlaylists(val playlists: List<GenericPlaylist>) : Host()
    }
}

@Serializable
sealed class GameAction : UserAction() {
    @Serializable sealed class Question : GameAction() {
        abstract val questionIndex: Int
        @Serializable data class AnswerQuestion(override val questionIndex: Int, val answer: UserAnswer) : Question()
        @Serializable data class RequestHint(override val questionIndex: Int, val hint: Hint) : Question()
    }

    @Serializable sealed class Answer : GameAction() {
        @Serializable object NextScreen : Answer()
    }
}

@Serializable
sealed class ServerAction : Action() {
    @Serializable data class StartGame(val questions: List<ServerGameQuestion>, val config: GameConfig, val userStates: Map<User, UserState>) : ServerAction()
    @Serializable data class RanOutOfTime(val unansweredUsers: Map<User, UserState>) : ServerAction()
}



@Serializable
data class ActionWithUser(val action: Action, val user: User)

fun UserAction.serialize() = Json.encodeToString(UserAction.serializer(), this)