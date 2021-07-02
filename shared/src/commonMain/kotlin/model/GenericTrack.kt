import com.adamratzman.spotify.models.Track
import kotlinx.serialization.Serializable

@Serializable
data class GenericTrack(
    val id: String,
    val name: String,
    val artists: List<GenericArtist>,
    val album: GenericAlbum
)

fun Track.toGenericTrack() = GenericTrack(
    this.id,
    this.name,
    this.artists.map { it.toGenericArtist() },
    this.album.toGenericAlbum()
)