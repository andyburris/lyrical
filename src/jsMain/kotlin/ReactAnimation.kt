import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.awaitAnimationFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.css.CSSBuilder
import kotlinx.css.LinearDimension
import kotlinx.css.height
import kotlinx.css.properties.FillMode
import kotlinx.css.properties.ms
import kotlinx.html.DIV
import org.w3c.dom.Element
import react.*
import styled.StyledDOMBuilder
import styled.animation
import styled.css
import styled.styledDiv
import kotlin.js.Date
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

@OptIn(ExperimentalTime::class)
data class AnimationSpec(val easing: Easing = { it }, val duration: Duration = 300.milliseconds, val delay: Duration = 0.milliseconds)

@OptIn(ExperimentalTime::class)
fun tween(duration: Duration = 300.milliseconds, delay: Duration = 0.milliseconds) = AnimationSpec(duration = duration, delay = delay)

typealias Easing = (timeProgress: Double) -> Double

fun <T> RBuilder.animate(value: T, animationSpec: AnimationSpec = tween(), converter: (old: T, new: T, progress: Double) -> T): T {
    val (currentState, setState) = useState(value)
    val animationProgress = useAnimation(animationSpec, dependencies = listOf(value))
    useEffect(listOf(animationProgress)) {
        setState(converter(currentState, value, animationProgress))
    }
    return currentState
}

@OptIn(ExperimentalTime::class)
fun RBuilder.useAnimation(animationSpec: AnimationSpec, dependencies: RDependenciesList = emptyList()): Double {
    val elapsed = useAnimationTimer(animationSpec.duration, animationSpec.delay, dependencies)
    val progress = min(1.0, elapsed / animationSpec.duration);

    // Return altered value based on our specified easing function
    return animationSpec.easing.invoke(progress)
}

@OptIn(ExperimentalTime::class)
fun RBuilder.useAnimationTimer(duration: Duration = 1000.milliseconds, delay: Duration = 0.milliseconds, dependencies: RDependenciesList = emptyList()): Duration {
    val (elapsed, setTime) = useState(0.milliseconds)
    useEffectWithCleanup(dependencies = dependencies + listOf(duration, delay)) {
        val start = window.performance.now()

        val job = GlobalScope.launch {
            delay(delay)
            while (elapsed < duration) {
                setTime((window.awaitAnimationFrame() - start).milliseconds.coerceAtMost(duration))
            }
        }

        return@useEffectWithCleanup {
            job.cancel()
        }
    }
    return elapsed
}

fun RBuilder.animate(value: Int, animationSpec: AnimationSpec = tween()): Int = animate(value, animationSpec) { old, new, progress -> (old + (new - old) * progress).toInt() }
fun RBuilder.animate(value: Double, animationSpec: AnimationSpec = tween()): Double = animate(value, animationSpec) { old, new, progress -> old + (new - old) * progress }
fun RBuilder.animate(value: Float, animationSpec: AnimationSpec = tween()): Float = animate(value, animationSpec) { old, new, progress -> old + (new - old) * progress.toFloat() }
fun RBuilder.animate(value: LinearDimension, animationSpec: AnimationSpec = tween()): LinearDimension = animate(value, animationSpec) { old, new, progress -> old + (new - old) * progress }

fun RBuilder.AnimatedVisibility(visible: Boolean, content: StyledDOMBuilder<DIV>.() -> Unit) {
    val (contentHeight, setContentHeight) = useState<LinearDimension?>(null)

    styledDiv {
        css {
            animation(200.ms, fillMode = FillMode.both) {
                0 {
                    height = LinearDimension.maxContent
                }
            }
        }
        content()

    }
}

@OptIn(ExperimentalTime::class)
fun <T> CSSBuilder.animate(stateHolder: AnimationStateHolder<T>, animationSpec: AnimationSpec = tween(), key: T, styles: CSSBuilder.() -> Unit) {
    val (oldStyles, setOldStyles) = stateHolder.state
    if (oldStyles != null) {
        println("getting old styles, oldStyles.styles =${oldStyles.styles}, styles = $styles")
        if (oldStyles.key != key) {
            println("rendering animation")
            animation(duration = animationSpec.duration.inMilliseconds.ms, delay = animationSpec.delay.inMilliseconds.ms, fillMode = FillMode.both) {
                0 { oldStyles.styles.invoke(this) }
                100 { styles.invoke(this) }
            }
            setOldStyles(CSSAnimation(key, styles))
            println("setting old styles, oldStyles.styles == styles = ${oldStyles.styles == styles}")
            println("setting old styles, oldStyles.key == key = ${oldStyles.key == key}")
        }
    } else {
        println("rendering initial set")
        styles.invoke(this)
        setOldStyles(CSSAnimation(key, styles))
        println("setting old styles, oldStyles.styles == styles = ${oldStyles?.styles == styles}")
        println("setting old styles, oldStyles.key == key = ${oldStyles?.key == key}")
    }

}

data class CSSAnimation<T>(val key: T, val styles: CSSBuilder.() -> Unit)
data class AnimationStateHolder<T>(val state: RStateDelegate<CSSAnimation<T>?> = useState(null))