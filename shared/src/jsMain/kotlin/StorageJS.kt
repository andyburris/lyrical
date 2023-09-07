import kotlinx.browser.localStorage
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set

actual var savedConfig
    get() = localStorage[CONFIG_KEY]?.let {
        println("backingConfigString = $it")
        Json.decodeFromString(it)
    } ?: GameConfig()
    set(value) {
        localStorage[CONFIG_KEY] = Json.encodeToString(value)
    }

actual var savedPlaylistURIs
    get() = localStorage[PLAYLISTS_KEY]?.let {
        println("backingPlaylistURIs = $it")
        Json.decodeFromString(ListSerializer(String.serializer()), it)
    } ?: emptyList()
    set(value) {
        localStorage[PLAYLISTS_KEY] = Json.encodeToString(ListSerializer(String.serializer()), value)
    }