package site.dogether.presentation.screen.splash

import androidx.compose.runtime.Immutable
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
data class SplashUiState(
    val isPermissionDialogShowing: Boolean = false,
    val deeplink: String? = null
)

sealed interface SplashUiEvent : UiEvent {
    sealed interface Lifecycle : SplashUiEvent {
        data object OnStart : Lifecycle
    }

    sealed interface Callback : SplashUiEvent {
        data class OnGetAppVersion(val appVersion: String) : Callback
    }

    sealed interface Deeplink : SplashUiEvent {
        data class OnDeeplinkReceived(val link: String?) : Deeplink
    }

    data class ShowToast(val text: String) : SplashUiEvent
}

sealed interface SplashUiEffect : UiEffect {
    data object GetAppVersion : SplashUiEffect
}