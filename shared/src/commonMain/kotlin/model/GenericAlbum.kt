import com.adamratzman.spotify.models.SimpleAlbum
import kotlinx.serialization.Serializable
import com.adamratzman.spotify.models.Album as SpotifyAlbum

@Serializable
data class GenericAlbum(
    val id: String,
    val name: String,
    val artists: List<GenericArtist>,
    val imageURL: String,
)

fun SpotifyAlbum.toGenericAlbum() = GenericAlbum(
    this.id,
    this.name,
    this.artists.map { it.toGenericArtist() },
    this.images.first().url
)

fun SimpleAlbum.toGenericAlbum() = GenericAlbum(
    this.id,
    this.name,
    this.artists.map { it.toGenericArtist() },
    this.images.first().url
)