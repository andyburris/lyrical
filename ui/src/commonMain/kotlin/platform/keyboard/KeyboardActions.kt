package platform.keyboard

/**
 * The [KeyboardActions] class allows developers to specify actions that will be triggered in
 * response to users triggering IME action on the software keyboard.
 */
class KeyboardActions(
    /**
     * This is run when the user triggers the [Done][ImeAction.Done] action. A null value
     * indicates that the default implementation if any, should be executed.
     */
    val onDone: (KeyboardActionScope.() -> Unit)? = null,

    /**
     * This is run when the user triggers the [Go][ImeAction.Go] action. A null value indicates
     * that the default implementation if any, should be executed.
     */
    val onGo: (KeyboardActionScope.() -> Unit)? = null,

    /**
     * This is run when the user triggers the [Next][ImeAction.Next] action. A null value
     * indicates that the default implementation should be executed. The default implementation
     * moves focus to the next item in the focus traversal order.
     *
     * See [Modifier.focusOrder()][androidx.compose.ui.focus.focusOrder] for more details on how
     * to specify a custom focus order if needed.
     */
    val onNext: (KeyboardActionScope.() -> Unit)? = null,

    /**
     * This is run when the user triggers the [Previous][ImeAction.Previous] action. A null value
     * indicates that the default implementation should be executed. The default implementation
     * moves focus to the previous item in the focus traversal order.
     *
     * See [Modifier.focusOrder()][androidx.compose.ui.focus.focusOrder] for more details on how
     * to specify a custom focus order if needed.
     */
    val onPrevious: (KeyboardActionScope.() -> Unit)? = null,

    /**
     * This is run when the user triggers the [Search][ImeAction.Search] action. A null value
     * indicates that the default implementation if any, should be executed.
     */
    val onSearch: (KeyboardActionScope.() -> Unit)? = null,

    /**
     * This is run when the user triggers the [Send][ImeAction.Send] action. A null value
     * indicates that the default implementation if any, should be executed.
     */
    val onSend: (KeyboardActionScope.() -> Unit)? = null
) {
    companion object {
        /**
         * Use this default value if you don't want to specify any action but want to use use the
         * default action implementations.
         */
        val Default: KeyboardActions = KeyboardActions()
    }
}

/**
 * Creates an instance of [KeyboardActions] that uses the specified lambda for all [ImeAction]s.
 */
fun KeyboardActions(onAny: KeyboardActionScope.() -> Unit): KeyboardActions = KeyboardActions(
    onDone = onAny,
    onGo = onAny,
    onNext = onAny,
    onPrevious = onAny,
    onSearch = onAny,
    onSend = onAny
)

/**
 * This scope can be used to execute the default action implementation.
 */
interface KeyboardActionScope {
    /**
     * Runs the default implementation for the specified [action][ImeAction].
     */
    fun defaultKeyboardAction(imeAction: ImeAction)
}
