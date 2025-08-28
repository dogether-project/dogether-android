package site.dogether.presentation.screen.force_update

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class ForceUpdateViewModel : BaseViewModel<ForceUpdateUiState>(ForceUpdateUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

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