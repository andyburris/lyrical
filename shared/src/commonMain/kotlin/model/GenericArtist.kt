import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.SimpleArtist
import kotlinx.serialization.Serializable

@Serializable
data class GenericArtist(
    val id: String,
    val name: String
)

fun Artist.toGenericArtist() = GenericArtist(
    this.id,
    this.name
)

fun SimpleArtist.toGenericArtist() = GenericArtist(
    this.id,
    this.name
)