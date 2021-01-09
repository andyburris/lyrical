import com.adamratzman.spotify.*
import com.adamratzman.spotify.endpoints.public.SearchApi
import com.adamratzman.spotify.endpoints.public.UserApi
import com.adamratzman.spotify.models.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


fun String.playlistURIFromURL() = this.removePrefix("https://open.spotify.com/playlist/").takeWhile { it != '?' }


class SpotifyRepository(val clientID: String, private val clientSecret: String) {
    var apiBacking: Deferred<SpotifyAppApi>? = null
    private suspend fun api(): SpotifyAppApi {
        if (apiBacking == null){
            apiBacking = coroutineScope {
                async {
                    spotifyAppApi(clientID, clientSecret).build()
                }
            }
        }
        return apiBacking!!.await()
    }

    suspend fun getPlaylistTracks(playlistURI: String): List<Track> {
        val playlistTracks = api().playlists.getPlaylistTracks(playlistURI)
        return playlistTracks
            .getAllItemsNotNull()
            .map { it.track }
            .filterIsInstance<Track>()
    }
    suspend fun getFeaturedPlaylists(): List<SimplePlaylist> = api().browse.getFeaturedPlaylists().playlists.items
    suspend fun searchPlaylists(query: String): List<SimplePlaylist> = api().search.search(query, SearchApi.SearchType.PLAYLIST).playlists?.items ?: emptyList()
    suspend fun getPlaylistByURL(url: String): SimplePlaylist? = getPlaylistByURI(url.playlistURIFromURL())
    suspend fun getUserPlaylists(): List<SimplePlaylist>? = (api() as? SpotifyClientApi)?.run { playlists.getClientPlaylists().getAllItems().filterNotNull() }
    suspend fun getPlaylistByURI(uri: String): SimplePlaylist? = api().playlists.getPlaylist(uri)?.toSimplePlaylist()
}

expect suspend fun SpotifyRepository.userAPI(): SpotifyClientApi?

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