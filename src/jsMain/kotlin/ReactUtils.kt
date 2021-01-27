import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import react.*
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv
import kotlin.Float
import kotlin.js.Date
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

val Event.targetInputValue: String
    get() = (target as? HTMLInputElement)?.value ?: (target as? HTMLTextAreaElement)?.value ?: ""

fun INPUT.onTextChanged(block: (String) -> Unit) {
    onChangeFunction = {
        val value = it.targetInputValue
        block.invoke(value)
    }
}

fun RBuilder.flexbox(direction: FlexDirection = FlexDirection.row, justifyContent: JustifyContent = JustifyContent.inherit, alignItems: Align = Align.inherit, gap: LinearDimension = 0.px, wrap: FlexWrap = FlexWrap.inherit, content: StyledDOMBuilder<DIV>.() -> Unit) {
    styledDiv {
        css {
            display = Display.flex
            flexDirection = direction
            flexWrap = wrap
            this.justifyContent = justifyContent
            this.alignItems = alignItems
            this.gap = Gap(gap.toString())
        }
        content.invoke(this)
    }
}

fun RBuilder.Screen(content: StyledDOMBuilder<DIV>.() -> Unit) {
    styledDiv {
        css {
            width = 100.pct
            minHeight = 100.vh
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.stretch
            justifyContent = JustifyContent.center
            padding(vertical = 128.px, horizontal = 196.px)
            boxSizing = BoxSizing.borderBox
        }
        content.invoke(this)
    }
}

data class Size(val width: LinearDimension, val height: LinearDimension) {
    constructor(size: LinearDimension) : this(size, size)
}
fun CSSBuilder.size(size: Size) {
    this.width = size.width
    this.height = size.height
}

fun CSSBuilder.size(linearDimension: LinearDimension) {
    this.width = linearDimension
    this.height = linearDimension
}

fun <T> StateFlow<T>.collectAsState(): T {
    val (state, setState) = useState(this.value)
    useEffectWithCleanup {
        val job = this.onEach {
            if (it != state) {
                setState(it)
            }
        }.launchIn(GlobalScope)
        return@useEffectWithCleanup { job.cancel() }
    }
    return state
}


/**
 * Hook that alerts clicks outside of the passed ref
 */
fun RBuilder.useOnOutsideClick(ref: RMutableRef<HTMLElement?>, onClick: () -> Unit) {
    useEffectWithCleanup {
        fun handleClickOutside(event: Event) {
            val current = ref.current ?: return
            if (!current.contains(event.target as HTMLElement)) {
                onClick.invoke()
            }
        }

        document.addEventListener("click", ::handleClickOutside)
        return@useEffectWithCleanup {
            document.removeEventListener("click", ::handleClickOutside)
        }
    }
}
