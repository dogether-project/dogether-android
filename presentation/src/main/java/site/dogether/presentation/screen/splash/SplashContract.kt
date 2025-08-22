package site.dogether.presentation.screen.splash

data class SplashUiState(
    val isPermissionDialogShowing: Boolean = false,
)

sealed interface SplashUiEvent {
    sealed interface Lifecycle : SplashUiEvent {
        data object OnStart : Lifecycle
    }

    sealed interface Callback : SplashUiEvent {
        data class OnGetAppVersion(val appVersion: String) : Callback
    }
}

sealed interface SplashUiEffect {
    data object GetAppVersion : SplashUiEffect

    data object NavigateToOnBoarding : SplashUiEffect

    data object NavigateToHome : SplashUiEffect

    data object NavigateToParticipationMethod : SplashUiEffect
}