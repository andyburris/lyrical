package client

import GameConfig
import Room.Client as ClientRoom
import User
import server.ServerError
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.GenericGameTrack
import model.GenericPlaylist
import server.ClientRoomState
import server.RoomState

@Serializable
sealed class ClientResponse {
    @Serializable data class Error(val error: ServerError) : ClientResponse()
    @Serializable data class Joined(val currentRoom: ClientRoom) : ClientResponse()
    @Serializable object Left : ClientResponse()
    @Serializable object Kicked : ClientResponse()
    @Serializable sealed class Room : ClientResponse() {
        @Serializable data class UsersUpdated(val users: List<User>, val host: User) : Room()
        @Serializable sealed class Lobby : Room() {
            @Serializable data class PlaylistsUpdated(val playlists: List<GenericPlaylist>) : Lobby()
            @Serializable data class ConfigUpdated(val config: GameConfig) : Lobby()
        }
        @Serializable object Loading : Room()
        @Serializable sealed class Game : Room() {
            @Serializable data class SendQuestion(val questionNumber: Int, val lyric: String, val sourcePlaylist: SourcePlaylist) : Game()
            @Serializable sealed class SendHint : Game() {
                @Serializable data class NextLyric(val questionNumber: Int, val lyric: String) : SendHint()
                @Serializable data class Artist(val questionNumber: Int, val artist: String) : SendHint()
            }
            @Serializable data class SendAnswer(val questionNumber: Int, val answer: ClientGameQuestion.Answered) : Game()
            @Serializable data class UpdateLeaderboard(val player: LeaderboardPlayer) : Game()
            @Serializable object Ended : Game()
        }
    }
}

private val module = SerializersModule {
    polymorphic(ClientRoomState::class) {
        subclass(RoomState.Lobby::class)
        subclass(RoomState.Loading::class)
        subclass(RoomState.Game.Client::class)
    }
}
private val jsonWithInterfaceSerializer = Json { serializersModule = module }
fun ClientResponse.serialize() = jsonWithInterfaceSerializer.encodeToString(ClientResponse.serializer(), this) //TODO: once sealed interfaces are automatically converted by kotlinx.serialization, replace
fun String.deserializeToClientResponse() = jsonWithInterfaceSerializer.decodeFromString(ClientResponse.serializer(), this)