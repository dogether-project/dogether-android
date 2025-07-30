package site.dogether.presentation.screen.on_boarding

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.screen.on_boarding.OnBoardingUiEvent.Click.OnClickKakaoLogin

class OnBoardingViewModel : BaseViewModel<OnBoardingUiState, OnBoardingUiEvent, OnBoardingUiEffect>(OnBoardingUiState()) {

    override fun onEvent(event: OnBoardingUiEvent) {
        when (event) {
            is OnBoardingUiEvent.Click -> {
                when (event) {
                    is OnClickKakaoLogin -> {

                    }
                }
            }
        }
    }
}