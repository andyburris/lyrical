package ui.khakra

import com.github.mpetuska.khakra.system.ChakraProps
import recordOf
import kotlinext.js.Record
import recordOf

data class ComponentStyle(
    /**Styles for the base style**/
    val baseStyle: Record<String, String> = recordOf(),
    /**Styles for the size variations**/
    val sizes: Record<String, Record<String, String>> = recordOf(),
    /**Styles for the visual style variations*/
    val variants: Record<String, Record<String, Any>> = recordOf(),
    /**The default `size` or `variant` values**/
    //val defaultProps: ComponentStyleDefaults = ,
)

fun ComponentStyle.toRecord() = recordOf(
    "baseStyle" to baseStyle,
    "sizes" to sizes,
    "variants" to variants
)

data class ComponentStyleDefaults(val size: String, val variant: String)