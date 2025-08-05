package site.dogether.presentation.screen.error

import site.dogether.presentation.base.BaseViewModel

class ErrorViewModel : BaseViewModel<ErrorUiState, ErrorUiEvent, ErrorUiEffect>(ErrorUiState()) {

    override fun onEvent(event: ErrorUiEvent) {
        when (event) {
            else -> Unit
        }
    }
}