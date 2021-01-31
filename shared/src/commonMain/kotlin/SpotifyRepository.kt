import com.adamratzman.spotify.*
import com.adamratzman.spotify.endpoints.public.SearchApi
import com.adamratzman.spotify.models.*


fun String.playlistURIFromURL() = this.removePrefix("https://open.spotify.com/playlist/").takeWhile { it != '?' }

sealed class SpotifyRepository {
    data class LoggedIn(val api: SpotifyClientApi) : SpotifyRepository() {
        suspend fun getPlaylistTracks(playlistURI: String): List<Track> {
            val playlistTracks = api.playlists.getPlaylistTracks(playlistURI)
            return playlistTracks
                .getAllItemsNotNull()
                .map { it.track }
                .filterIsInstance<Track>()
        }
        suspend fun getFeaturedPlaylists(): List<SimplePlaylist> = api.browse.getFeaturedPlaylists().playlists.items
        suspend fun searchPlaylists(query: String): List<SimplePlaylist> = api.search.search(query, SearchApi.SearchType.PLAYLIST).playlists?.items ?: emptyList()
        suspend fun getPlaylistByURL(url: String): SimplePlaylist? = getPlaylistByURI(url.playlistURIFromURL())
        suspend fun getUserPlaylists(): List<SimplePlaylist> = api.playlists.getClientPlaylists().getAllItems().filterNotNull()
        suspend fun getPlaylistByURI(uri: String): SimplePlaylist? = api.playlists.getPlaylist(uri)?.toSimplePlaylist()
    }
    object LoggedOut : SpotifyRepository()
}

private fun Playlist.toSimplePlaylist() = SimplePlaylist(
    externalUrlsString = externalUrls.map { it.name to it.url }.toMap(),
    href = href,
    id = id,
    uri = uri,
    collaborative = collaborative,
    images = images,
    name = name,
    description = description,
    owner = owner,
    primaryColor = primaryColor,
    public = public,
    snapshotIdString = snapshot.snapshotId,
    tracks = PlaylistTrackInfo(tracks.href, tracks.total),
    type = type
)