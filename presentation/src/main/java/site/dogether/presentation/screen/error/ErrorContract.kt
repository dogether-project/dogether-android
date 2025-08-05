package site.dogether.presentation.screen.error

import site.dogether.presentation.screen.error.model.Error

data class ErrorUiState(
    val error: Error? = Error.InvalidGroup
)

sealed interface ErrorUiEvent {

}

sealed interface ErrorUiEffect {

}