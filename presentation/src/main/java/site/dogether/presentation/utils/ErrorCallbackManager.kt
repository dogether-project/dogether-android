package site.dogether.presentation.utils

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * ErrorScreen에서 사용할 콜백을 저장하는 매니저
 * Navigation으로 람다를 전달할 수 없기 때문에 이 매니저를 통해 콜백을 저장/실행합니다.
 */
class ErrorCallbackManager {
    private var onPositiveCallback: (() -> Unit)? = null
    private var onNegativeCallback: (() -> Unit)? = null

    fun setCallbacks(
        onPositive: (() -> Unit)? = null,
        onNegative: (() -> Unit)? = null
    ) {
        onPositiveCallback = onPositive
        onNegativeCallback = onNegative
    }

    fun executePositiveCallback() {
        onPositiveCallback?.invoke()
        clearCallbacks()
    }

    fun executeNegativeCallback() {
        onNegativeCallback?.invoke()
        clearCallbacks()
    }

    fun clearCallbacks() {
        onPositiveCallback = null
        onNegativeCallback = null
    }
}

val LocalErrorCallbackManager = staticCompositionLocalOf<ErrorCallbackManager> {
    error("ErrorCallbackManager not provided")
}

