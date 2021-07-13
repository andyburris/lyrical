package data

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Sequential collection of suspend functions to be executed in order
 */
val LocalEffect = compositionLocalOf { MutableSharedFlow<suspend CoroutineScope.() -> Unit>() }

@Composable
fun ProvideLocalEffect(effectHandler: MutableSharedFlow<suspend CoroutineScope.() -> Unit>, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalEffect provides effectHandler, content = content)
}

@Composable
fun LocalEffect(scope: CoroutineScope, effect: suspend CoroutineScope.() -> Unit) {
    val currentLocalEffect = LocalEffect.current
    remember {
        scope.launch {
            currentLocalEffect.emit(effect)
        }
    }

}