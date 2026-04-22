package site.dogether.presentation.screen.my_page

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.user.GetProfileUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error

class MyPageViewModel(
    private val getProfileUseCase: GetProfileUseCase,
) : BaseViewModel<MyPageUiState>(MyPageUiState()) {


    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            MyPageUiEvent.OnStart -> {
                getProfile()
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

    private fun getProfile() {
        viewModelScope.launch {
            getProfileUseCase().onSuccess { result ->
                updateState { it.copy(profile = result) }
            }.onFailure {
                postEffect(
                    UiEffect.NavigateToErrorWithCallback(
                        error = Error.LoadData,
                        onPositive = { getProfile() }
                    )
                )
            }
        }
    }
}