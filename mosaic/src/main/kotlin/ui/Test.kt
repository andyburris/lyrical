package ui

import androidx.compose.runtime.*
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import data.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Test() {
    val terminal = LocalTerminal.current
    val presses = remember { mutableStateListOf<Keycode>() }
    Column {
        Text("Presses:")
        presses.forEach {
            Text(it.toString())
        }
    }

    val compositionScope = rememberCoroutineScope()
    LocalEffect(compositionScope) {
        val pressesStream: SharedFlow<Keycode> = terminal.presses
/*        val aStream = pressesStream.filter { it == Key.A.pressed() }
        val bStream = pressesStream.filter { it == Key.B.pressed() }
        merge(aStream, bStream).collect { keycode ->
            presses += keycode
        }*/
        pressesStream.onEach { keycode ->
            presses += keycode
        }.launchIn(compositionScope)
    }
}