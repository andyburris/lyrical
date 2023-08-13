import com.adamratzman.spotify.*
import com.adamratzman.spotify.endpoints.pub.SearchApi
import com.adamratzman.spotify.models.*

fun String.playlistURIFromURL() = this.removePrefix("https://open.spotify.com/playlist/").takeWhile { it != '?' }

sealed class SpotifyRepository() {
    data class LoggedIn(val api: SpotifyClientApi) : SpotifyRepository() {
        internal val cachedPlaylists = mutableListOf<SimplePlaylist>()
        suspend fun getPlaylistTracks(playlistURI: String): List<Track> {
            val playlistTracks = api.playlists.getPlaylistTracks(playlistURI)
            return playlistTracks
                .getAllItemsNotNull()
                .map { it.track }
                .filterIsInstance<Track>()
        }
        suspend fun getFeaturedPlaylists(): List<SimplePlaylist> = api.browse.getFeaturedPlaylists().playlists.items.also { cachedPlaylists.addAll(it); cachedPlaylists.retainDistinct() }
        suspend fun searchPlaylists(query: String): List<SimplePlaylist> = api.search.search(query, SearchApi.SearchType.Playlist).playlists?.items ?: emptyList()
        suspend fun getPlaylistByURL(url: String): SimplePlaylist? = getPlaylistByURI(url.playlistURIFromURL())?.also { cachedPlaylists.add(it); cachedPlaylists.retainDistinct() }
        suspend fun getPlaylistByURI(uri: String): SimplePlaylist? = cachedPlaylists.find { it.uri.uri == uri } ?: api.playlists.getPlaylist(uri)?.toSimplePlaylist()?.also { cachedPlaylists.add(it); cachedPlaylists.retainDistinct() }

        suspend fun getUserPlaylists(): List<SimplePlaylist> = api.playlists.getClientPlaylists().getAllItems().filterNotNull().also { cachedPlaylists.addAll(it); cachedPlaylists.retainDistinct() }
    }
    object LoggedOut : SpotifyRepository()
}

private fun MutableList<SimplePlaylist>.retainDistinct() = this.toMutableSet().toMutableList()

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