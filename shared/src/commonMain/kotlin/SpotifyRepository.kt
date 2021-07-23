import com.adamratzman.spotify.*
import com.adamratzman.spotify.endpoints.pub.SearchApi
import com.adamratzman.spotify.models.*
import io.ktor.client.*
import io.ktor.client.request.*
import model.GenericPlaylist

sealed interface SpotifyRepository {
    suspend fun getPlaylistTracks(playlistURI: String): List<Track>
    suspend fun getArtistTracks(artistURI: String): List<Track>
    suspend fun getFeaturedPlaylists(): List<SimplePlaylist>
    suspend fun searchPlaylists(query: String): List<SimplePlaylist>
    suspend fun getPlaylistByURI(playlistURI: String): SimplePlaylist
}


open class LocalSpotifyRepository(open val api: SpotifyApi<*, *>) : SpotifyRepository {
    internal val cachedPlaylists = mutableListOf<SimplePlaylist>()
    override suspend fun getPlaylistTracks(playlistURI: String): List<Track> {
        val playlistTracks = api.playlists.getPlaylistTracks(playlistURI)
        return playlistTracks
            .getAllItemsNotNull()
            .map { it.track }
            .filterIsInstance<Track>()
    }

    override suspend fun getArtistTracks(artistURI: String): List<Track> {
        val artist = api.artists.getArtist(artistURI) ?: throw Error("Artist does not exist")
        val searchResults = api.search.search("artist:\"${artist.name}\"").tracks?.getAllItemsNotNull() ?: emptyList()
        return searchResults.filter { result -> result.artists.any { it.id == artist.id } }.distinctBy { it.id } // since other artists with similar names might be listed, filter those out
    }
    override suspend fun getFeaturedPlaylists(): List<SimplePlaylist> = api.browse.getFeaturedPlaylists().playlists.items.also { cachedPlaylists.addAll(it); cachedPlaylists.retainDistinct() }
    override suspend fun searchPlaylists(query: String): List<SimplePlaylist> = api.search.search(query, SearchApi.SearchType.PLAYLIST).playlists?.items ?: emptyList()
    override suspend fun getPlaylistByURI(playlistURI: String): SimplePlaylist = cachedPlaylists.find { it.uri.uri == playlistURI } ?: api.playlists.getPlaylist(playlistURI)?.toSimplePlaylist()?.also { cachedPlaylists.add(it); cachedPlaylists.retainDistinct() } ?: throw Error("No playlist with that uri")
}

data class ClientSpotifyRepository(override val api: SpotifyClientApi) : LocalSpotifyRepository(api) {
    suspend fun getUserPlaylists(): List<SimplePlaylist> = api.playlists.getClientPlaylists().getAllItems().filterNotNull().also { cachedPlaylists.addAll(it); cachedPlaylists.retainDistinct() }
}

data class RemoteSpotifyRepository(val httpClient: HttpClient) : SpotifyRepository {
    override suspend fun getPlaylistTracks(playlistURI: String): List<Track> = httpClient.get("$serverURL/playlistTracks/$playlistURI")
    override suspend fun getArtistTracks(artistURI: String): List<Track> = httpClient.get("$serverURL/artistTracks/$artistURI")
    override suspend fun getFeaturedPlaylists(): List<SimplePlaylist> = httpClient.get("$serverURL/featured")
    override suspend fun searchPlaylists(query: String): List<SimplePlaylist> = httpClient.get("$serverURL/search/$query")
    override suspend fun getPlaylistByURI(playlistURI: String): SimplePlaylist = httpClient.get("$serverURL/playlist/$playlistURI")
}

suspend fun SpotifyRepository.getGenericPlaylistTracks(playlist: GenericPlaylist) = when(playlist) {
    is GenericPlaylist.Playlist -> this.getPlaylistTracks(playlist.id)
    is GenericPlaylist.Artist -> this.getArtistTracks(playlist.artist.id)
}

fun String.playlistURIFromURL() = this.removePrefix("https://open.spotify.com/playlist/").takeWhile { it != '?' }
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

