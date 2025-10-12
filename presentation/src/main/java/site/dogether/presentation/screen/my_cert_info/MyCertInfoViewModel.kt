package site.dogether.presentation.screen.my_cert_info

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class MyCertInfoViewModel : BaseViewModel<MyCertInfoUiState>(MyCertInfoUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is MyCertInfoUiEvent.Click -> {
                when (event) {
                    is MyCertInfoUiEvent.Click.OnClickItem -> {
                        updateState { it.copy(selectedItemIndex = event.index) }
                    }
                }
            }
        }
    }
}