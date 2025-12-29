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

    sealed interface Deeplink : SplashUiEvent {
        data class OnDeeplinkReceived(val link: String?) : Deeplink
    }
}

sealed interface SplashUiEffect : UiEffect {
    data object GetAppVersion : SplashUiEffect

    data object NavigateToOnBoarding : SplashUiEffect

    data object NavigateToReviewCertification : SplashUiEffect

    data object NavigateToHome : SplashUiEffect

    data object NavigateToParticipationMethod : SplashUiEffect

    data class NavigateToParticipateGroup(val joinCode: String) : SplashUiEffect
}