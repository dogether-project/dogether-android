package site.dogether.presentation.screen.error

import androidx.lifecycle.SavedStateHandle
import site.dogether.KEY_ERROR_TYPE
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error

class ErrorViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ErrorUiState>(ErrorUiState()) {

    init {
        val errorTypeName = savedStateHandle.get<String>(KEY_ERROR_TYPE)
        val errorType = errorTypeName?.let { name ->
            Error.entries.find { it.name == name }
        } ?: Error.Unknown
        
        updateState { it.copy(error = errorType) }
    }

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is ErrorUiEvent.OnClickPositive -> {
                postEffect(ErrorUiEffect.ExecutePositiveCallback)
            }
            is ErrorUiEvent.OnClickNegative -> {
                postEffect(ErrorUiEffect.ExecuteNegativeCallback)
            }
            else -> Unit
        }
    }
}