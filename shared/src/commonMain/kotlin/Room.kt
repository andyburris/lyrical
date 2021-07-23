import client.ClientGameQuestion
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import server.*


@Serializable
sealed class Room {
    abstract val code: String
    abstract val host: User
    @Serializable
    data class Server(
        override val code: String,
        override val host: User,
        val state: ServerRoomState
    ) : Room()

    @Serializable
    data class Client(
        override val code: String,
        override val host: User,
        @Polymorphic val state: ClientRoomState
    ) : Room()
}

fun Room.Server.withLobby(transform: (RoomState.Lobby) -> RoomState.Lobby): Room.Server {
    val lobbyState = this.state as RoomState.Lobby
    return this.copy(state = transform(lobbyState))
}

fun Room.Server.withGame(transform: (RoomState.Game.Server) -> RoomState.Game.Server): Room.Server {
    val gameState = this.state as RoomState.Game.Server
    return this.copy(state = transform(gameState))
}

fun Room.Client.withLobby(transform: (RoomState.Lobby) -> RoomState.Lobby): Room.Client {
    val lobbyState = this.state as RoomState.Lobby
    return this.copy(state = transform(lobbyState))
}

fun Room.Client.withGame(transform: (RoomState.Game.Client) -> RoomState.Game.Client): Room.Client {
    val gameState = this.state as? RoomState.Game.Client ?: when(this.state) {
        is RoomState.Loading -> emptyGameState(this.state.config, this.state.users)
        else -> throw Error("Creating empty game state should only be called when room.state is RoomState.Loading")
    }
    return this.copy(state = transform(gameState))
}

fun emptyGameState(config: GameConfig, users: List<User>) = RoomState.Game.Client(config = config, questions = (0 until config.amountOfSongs).map { ClientGameQuestion.NotReached }, gameScreen = GameScreen.Answer(questionIndex = -1), leaderboard = users.associateWith { UserState(true, emptyList()) }.toLeaderboard())


val Room.Server.joinedUsers: List<User> get() = when(this.state) {
    is RoomState.Lobby -> this.state.joinedUsers
    is RoomState.Loading -> this.state.users
    is RoomState.Game.Server -> this.state.userStates.filterValues { it.connected }.keys.toList()
}

val Room.Client.joinedUsers: List<User> get() = when(this.state) {
    is RoomState.Lobby -> this.state.joinedUsers
    is RoomState.Loading -> this.state.users
    is RoomState.Game.Client -> this.state.leaderboard.players.filter { it.connected }.map { it.user }
}

fun Room.Server.toClientRoom(user: User): Room.Client = Room.Client(code, host, state.toClientRoomState(user))