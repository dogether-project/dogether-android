package site.dogether.presentation.screen.on_boarding

data class OnBoardingUiState(
    val isLoading: Boolean = false,
)

sealed interface OnBoardingUiEvent {
    sealed interface Click : OnBoardingUiEvent {
        object OnClickKakaoLogin : Click
    }

    sealed interface Callback : OnBoardingUiEvent {
        data class OnSuccessKakaoLogin(
            val name: String,
            val idToken: String,
        ) : Callback

        data class OnErrorKakaoLogin(val throwable: Throwable) : Callback
    }
}

sealed interface OnBoardingUiEffect {
    object LoginWithKakao : OnBoardingUiEffect
}