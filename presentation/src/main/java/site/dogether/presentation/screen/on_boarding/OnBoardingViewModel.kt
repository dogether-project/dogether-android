package site.dogether.presentation.screen.on_boarding

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.user.LoginWithKakaoUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.screen.on_boarding.OnBoardingUiEvent.Click.OnClickKakaoLogin

class OnBoardingViewModel(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
) : BaseViewModel<OnBoardingUiState, OnBoardingUiEvent, OnBoardingUiEffect>(OnBoardingUiState()) {

    override fun onEvent(event: OnBoardingUiEvent) {
        when (event) {
            is OnBoardingUiEvent.Click -> {
                when (event) {
                    is OnClickKakaoLogin -> {
                        postEffect(OnBoardingUiEffect.LoginWithKakao)
                    }
                }
            }

            is OnBoardingUiEvent.Callback -> {
                when (event) {
                    is OnBoardingUiEvent.Callback.OnSuccessKakaoLogin -> {
                        viewModelScope.launch {
                            loginWithKakaoUseCase(
                                name = event.name,
                                idToken = event.idToken
                            )
                        }
                    }

                    is OnBoardingUiEvent.Callback.OnErrorKakaoLogin -> Unit
                }
            }
        }
    }
}