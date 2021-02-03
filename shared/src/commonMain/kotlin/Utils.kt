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