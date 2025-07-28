package site.dogether.presentation.screen.splash

data class SplashUiState(
    val isPermissionDialogShowing: Boolean = false
)

sealed interface SplashUiEffect {
    data object CheckNotificationPermission : SplashUiEffect

    data object NavigateToNotificationSetting : SplashUiEffect
}