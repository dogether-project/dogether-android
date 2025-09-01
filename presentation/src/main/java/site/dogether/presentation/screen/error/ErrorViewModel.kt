package site.dogether.presentation.screen.error

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class ErrorViewModel : BaseViewModel<ErrorUiState>(ErrorUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            else -> Unit
        }
    }
}