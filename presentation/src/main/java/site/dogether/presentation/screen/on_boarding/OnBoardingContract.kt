package site.dogether.presentation.screen.on_boarding

import androidx.compose.runtime.Immutable
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
data class OnBoardingUiState(
    val isLoading: Boolean = false,
)

sealed interface OnBoardingUiEvent : UiEvent {
    sealed interface Click : OnBoardingUiEvent {
        object OnClickKakaoLogin : Click
    }

    sealed interface Callback : OnBoardingUiEvent {
        data class OnLoginWithKakaoTalkPossible(val isPossible: Boolean) : Callback
        data class OnSuccessKakaoLogin(
            val name: String,
            val idToken: String,
        ) : Callback

        data class OnErrorKakaoLogin(val throwable: Throwable) : Callback
    }
}

sealed interface OnBoardingUiEffect : UiEffect {
    object CheckLoginWithKakaoTalkPossibility : OnBoardingUiEffect

    object LoginWithKakaoTalk : OnBoardingUiEffect

    object LoginWithKakaoAccount : OnBoardingUiEffect

    object NavigateToHome : OnBoardingUiEffect

    data class NavigateToParticipateGroup(val joinCode: String) : OnBoardingUiEffect
}