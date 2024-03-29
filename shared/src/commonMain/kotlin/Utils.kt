import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


fun <T> List<T>.replace(index: Int, transform: (item: T) -> T) = this.mapIndexed { i, item -> if (i == index) transform(item) else item }
fun Int.distributeInto(amount: Int): List<Int> {
    val base = this / amount
    val remainder = this % amount
    return (0 until this).mapIndexed { index, i -> if (index < remainder) base + 1 else base }
}

class SourcedMutableStateFlow<T>(initialValue: T, private val updateSource: (T) -> Unit) : MutableStateFlow<T> {
    private val backingStateFlow = MutableStateFlow(initialValue)
    override val replayCache: List<T> = backingStateFlow.replayCache
    override val subscriptionCount: StateFlow<Int> = backingStateFlow.subscriptionCount
    override var value: T
        get() = backingStateFlow.value
        set(value) {
            updateSource.invoke(value)
            backingStateFlow.value = value
        }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) = backingStateFlow.collect(collector)

    override fun compareAndSet(expect: T, update: T): Boolean = backingStateFlow.compareAndSet(expect, update)

    override suspend fun emit(value: T) {
        updateSource.invoke(value)
        backingStateFlow.emit(value)
    }

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() = backingStateFlow.resetReplayCache()

    override fun tryEmit(value: T): Boolean {
        val success = backingStateFlow.tryEmit(value)
        if (success) updateSource.invoke(value)
        return success
    }
}

private val accentsMap = mapOf(
    'À' to 'A',
    'Á' to 'A',
    'Â' to 'A',
    'Ã' to 'A',
    'Ä' to 'A',
    'È' to 'E',
    'É' to 'E',
    'Ê' to 'E',
    'Ë' to 'E',
    'Í' to 'I',
    'Ì' to 'I',
    'Î' to 'I',
    'Ï' to 'I',
    'Ù' to 'U',
    'Ú' to 'U',
    'Û' to 'U',
    'Ü' to 'U',
    'Ò' to 'O',
    'Ó' to 'O',
    'Ô' to 'O',
    'Õ' to 'O',
    'Ö' to 'O',
    'Ñ' to 'N',
    'Ç' to 'C',
    'ª' to 'A',
    'º' to 'O',
    '§' to 'S',
    '³' to '3',
    '²' to '2',
    '¹' to '1',
    'à' to 'a',
    'á' to 'a',
    'â' to 'a',
    'ã' to 'a',
    'ä' to 'a',
    'è' to 'e',
    'é' to 'e',
    'ê' to 'e',
    'ë' to 'e',
    'í' to 'i',
    'ì' to 'i',
    'î' to 'i',
    'ï' to 'i',
    'ù' to 'u',
    'ú' to 'u',
    'û' to 'u',
    'ü' to 'u',
    'ò' to 'o',
    'ó' to 'o',
    'ô' to 'o',
    'õ' to 'o',
    'ö' to 'o',
    'ñ' to 'n',
    'ç' to 'c',
    'Ą' to 'A',
    'Ę' to 'E',
    'Ć' to 'C',
    'Ł' to 'L',
    'Ń' to 'N',
    'Ś' to 'S',
    'Ź' to 'Z',
    'Ż' to 'Z',
    'ą' to 'a',
    'ę' to 'e',
    'ç' to 'c',
    'ć' to 'c',
    'ł' to 'l',
    'ń' to 'n',
    'ś' to 's',
    'ź' to 'z',
    'ż' to 'z',
)
fun String.stripAccents() = this.map { (accentsMap[it] ?: it) as Char }.joinToString("")