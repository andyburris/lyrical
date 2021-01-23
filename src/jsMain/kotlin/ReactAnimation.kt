import kotlinx.browser.window
import react.*
import kotlin.js.Date
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

@OptIn(ExperimentalTime::class)
data class AnimationSpec(val easing: Easing = { it }, val duration: Duration = 1000.milliseconds, val delay: Duration = 0.milliseconds)

@OptIn(ExperimentalTime::class)
fun tween(duration: Duration = 1000.milliseconds, delay: Duration = 0.milliseconds) = AnimationSpec(duration = duration, delay = delay)

typealias Easing = (timeProgress: Double) -> Double

@OptIn(ExperimentalTime::class)
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
        var animationFrame: Int = -1
        var start = -1.milliseconds

        fun loop() {
            animationFrame = window.requestAnimationFrame {
                setTime(Date.now().milliseconds - start)
                loop()
            }
        }

        var durationHandle: Int? = null
        val delayHandle = window.setTimeout(timeout = delay.inMilliseconds.toInt(), handler = {
            durationHandle = window.setTimeout(timeout = duration.inMilliseconds.toInt(), handler = {
                window.cancelAnimationFrame(animationFrame)
                setTime(Date.now().milliseconds - start)
            })
            start = Date.now().milliseconds
            loop()
        })

        return@useEffectWithCleanup {
            window.cancelAnimationFrame(animationFrame)
            window.clearTimeout(delayHandle)
            durationHandle?.let { window.clearTimeout(it) }
        }
    }
    return elapsed
}