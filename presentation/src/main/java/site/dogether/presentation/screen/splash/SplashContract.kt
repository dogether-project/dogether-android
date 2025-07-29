package site.dogether.presentation.screen.splash

data class SplashUiState(
    val isPermissionDialogShowing: Boolean = false
)

sealed interface SplashUiEvent {
    sealed interface Lifecycle : SplashUiEvent {
        data object OnStart : Lifecycle
    }
}

sealed interface SplashUiEffect {

}