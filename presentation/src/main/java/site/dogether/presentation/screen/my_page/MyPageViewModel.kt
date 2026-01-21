package site.dogether.presentation.screen.my_page

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.user.GetUserInfoUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error

class MyPageViewModel(
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : BaseViewModel<MyPageUiState>(MyPageUiState()) {


    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            MyPageUiEvent.OnStart -> {
                getUserInfo()
            }

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

    private fun getUserInfo() {
        viewModelScope.launch {
            getUserInfoUseCase()
                .onSuccess { result ->
                    updateState { it.copy(userInfo = result) }
                }
                .onFailure {
                    postEffect(
                        UiEffect.NavigateToErrorWithCallback(
                            error = Error.LoadData,
                            onPositive = { getUserInfo() }
                        )
                    )
                }
        }
    }
}