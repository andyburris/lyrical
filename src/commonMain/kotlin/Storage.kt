import com.adamratzman.spotify.models.SimplePlaylist

const val CONFIG_KEY = "gameConfig"
const val PLAYLISTS_KEY = "currentPlaylistURIs"

expect var savedConfig: GameConfig
expect var savedPlaylistURIs: List<String>