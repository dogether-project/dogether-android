package site.dogether.presentation.screen.on_boarding

data class OnBoardingUiState(
    val isLoading: Boolean = false,
)

sealed interface OnBoardingUiEvent {
    sealed interface Click : OnBoardingUiEvent {
        object OnClickKakaoLogin : Click
    }
}

sealed interface OnBoardingUiEffect {

}