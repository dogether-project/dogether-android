package site.dogether.presentation.screen.splash

import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class SplashUiState(
    val isPermissionDialogShowing: Boolean = false,
)

sealed interface SplashUiEvent : UiEvent {
    sealed interface Lifecycle : SplashUiEvent {
        data object OnStart : Lifecycle
    }

    sealed interface Callback : SplashUiEvent {
        data class OnGetAppVersion(val appVersion: String) : Callback
    }
}

sealed interface SplashUiEffect : UiEffect {
    data object GetAppVersion : SplashUiEffect

    data object NavigateToOnBoarding : SplashUiEffect

    data object NavigateToHome : SplashUiEffect

    data object NavigateToParticipationMethod : SplashUiEffect
}