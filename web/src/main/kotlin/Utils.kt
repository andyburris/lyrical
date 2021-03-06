import com.adamratzman.spotify.models.Track
import com.github.mpetuska.khakra.theme.ChakraTheme
import com.soywiz.korio.jsObjectToMap
import com.soywiz.korio.toJsObject
import kotlinext.js.Record
import kotlinext.js.set
import ui.khakra.ChakraThemeConfig
import ui.khakra.toRecord

val Track.imageUrl get() = album.images.firstOrNull()?.url ?: "assets/AlbumPlaceholder.svg"

fun <V: Any> recordOf(vararg entries: Pair<String, V>) = Record<String, V> {
    entries.forEach { pair ->
        this[pair.first] = pair.second
    }
}
fun <V: Any> recordOf() = Record<String, V> {}
operator fun <V: Any> Record<String, V>.plus(other: Record<String, V>): Record<String, V> {
    val new = recordOf<V>()
    for ((key, value ) in jsObjectToMap(this)) {
        new[key] = value
    }
    for((key, value) in jsObjectToMap(other)) {
        new[key] = value
    }
    return new
}

operator fun <V: Any> Record<String, V>.plusAssign(other: Record<String, V>) {
    this.apply {
        for((key, value) in jsObjectToMap(other)) {
            this[key] = value
        }
    }
}
fun <V: Any> Record<String, V>.iterator(): Map<String, V> = jsObjectToMap(this)
fun <V: Any, R: Any> Record<String, V>.map(transform: (Map.Entry<String, V>) -> R): Record<String, R> = jsObjectToMap(this).mapValues { transform(it) }.toJsObject() as Record<String, R>