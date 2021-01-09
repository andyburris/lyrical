import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.models.Playable
import com.adamratzman.spotify.models.PlaylistTrack
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.spotifyAppApi
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val playlistURI = "spotify:playlist:37i9dQZF1DX5Ejj0EkURtP"
        val api = spotifyAppApi(clientID, clientSecret).build()
        val playlistTracks = api.playlists.getPlaylistTracks(playlistURI)
        val nextPagingObject = playlistTracks.getAllItemsNotNull() //fails here
        println(nextPagingObject)
        println(nextPagingObject.map { it.track }.filterIsInstance<Track>().map { it.name })
        println(nextPagingObject.size)
    }
}