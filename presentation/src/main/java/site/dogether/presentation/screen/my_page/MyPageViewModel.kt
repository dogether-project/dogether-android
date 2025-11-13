package site.dogether.presentation.screen.my_page

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class MyPageViewModel : BaseViewModel<MyPageUiState>(MyPageUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is MyPageUiEvent.Click -> {
                when (event) {
                    is MyPageUiEvent.Click.OnClickStatistics -> {
                        postEffect(MyPageUiEffect.NavigateToStatistics)
                    }

                    is MyPageUiEvent.Click.OnClickCertificationList -> {
                        postEffect(MyPageUiEffect.NavigateToCertificationList)
                    }

                    is MyPageUiEvent.Click.OnClickGroupManagement -> {
                        postEffect(MyPageUiEffect.NavigateToGroupManagement)
                    }

                    is MyPageUiEvent.Click.OnClickSettings -> {
                        postEffect(MyPageUiEffect.NavigateToSettings)
                    }
                }
            }
        }
    }
}