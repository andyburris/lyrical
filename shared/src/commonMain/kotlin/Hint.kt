import kotlinx.serialization.Serializable

@Serializable
sealed class Hint {
    @Serializable object NextLyric : Hint()
    @Serializable object Artist : Hint()
}
