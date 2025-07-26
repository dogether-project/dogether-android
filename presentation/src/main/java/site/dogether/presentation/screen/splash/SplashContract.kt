package site.dogether.presentation.screen.splash

data class SplashUiState(
    val isLoading: Boolean = false,
)

sealed interface SplashUiEffect {

}