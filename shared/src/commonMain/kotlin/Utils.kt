import com.adamratzman.spotify.models.Track

fun <T> List<T>.replace(index: Int, transform: (item: T) -> T) = this.mapIndexed { i, item -> if (i == index) transform(item) else item }
