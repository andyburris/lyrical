import kotlinx.serialization.Serializable
import model.GenericPlaylist

@Serializable
sealed class User() {
    @Serializable data class Anonymous(val id: String) : User()
    @Serializable data class Spotify(val username: String) : User()
}

val User.name get() = when(this) {
    is User.Anonymous -> "Anonymous"
    is User.Spotify -> username
}