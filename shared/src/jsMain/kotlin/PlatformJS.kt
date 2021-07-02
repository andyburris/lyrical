import kotlinx.browser.window

actual object BuildConfig {
    actual val debug: Boolean
        get() = window.location.hostname == "localhost"

}