package site.dogether.presentation.screen.force_update

import site.dogether.presentation.base.BaseViewModel

class ForceUpdateViewModel : BaseViewModel<ForceUpdateUiState, ForceUpdateUiEvent, ForceUpdateUiEffect>(ForceUpdateUiState()) {

    override fun onEvent(event: ForceUpdateUiEvent) {
        when (event) {
            is ForceUpdateUiEvent.Click -> {
                when (event) {
                    is ForceUpdateUiEvent.Click.OnClickUpdate -> {

                    }
                }
            }
        }
    }
}