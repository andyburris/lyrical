import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.css.*
import kotlinx.html.DIV
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.useEffectWithCleanup
import react.useState
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv

val Event.targetInputValue: String
    get() = (target as? HTMLInputElement)?.value ?: (target as? HTMLTextAreaElement)?.value ?: ""

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
            width = 100.vw
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

fun <T> StateFlow<T>.collectAsState(): Pair<T, (T) -> Unit> {
    val state: Pair<T, (value: T) -> Unit> = useState(this.value)
    useEffectWithCleanup {
        val job = this.onEach {
            if (it != state.first) {
                state.second(it)
            }
        }.launchIn(GlobalScope)
        return@useEffectWithCleanup { job.cancel() }
    }
    return state
}