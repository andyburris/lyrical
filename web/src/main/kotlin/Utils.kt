import com.adamratzman.spotify.models.Track

val Track.imageUrl get() = album.images.firstOrNull()?.url ?: "assets/AlbumPlaceholder.svg"