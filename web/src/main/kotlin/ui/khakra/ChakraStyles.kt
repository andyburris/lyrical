package ui.khakra

import com.github.mpetuska.khakra.kt.Builder
import com.github.mpetuska.khakra.system.ChakraProps
import kotlinext.js.Record
import kotlinx.css.CSSBuilder

data class ChakraStyles(
    val global: (ChakraProps) -> Record<String, Record<String, String>>
)

fun createStyles(block: CSSBuilder.() -> Unit) {
    val builder = CSSBuilder()
    block.invoke(builder)
    builder.rules
}

