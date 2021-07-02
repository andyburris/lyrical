import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
@OptIn(ExperimentalTime::class)
data class GameConfig(
    val amountOfSongs: Int = 10,
    val timer: Int = 0,
    val showSourcePlaylist: Boolean = false,
    val distributePlaylistsEvenly: Boolean = true,
    val difficulty: Difficulty = Difficulty.Medium
)

@Serializable
enum class Difficulty { Easy, Medium, Hard }