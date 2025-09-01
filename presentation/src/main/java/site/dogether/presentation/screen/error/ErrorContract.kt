package site.dogether.presentation.screen.error

import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error

data class ErrorUiState(
    val error: Error? = Error.InvalidGroup,
)

sealed interface ErrorUiEvent : UiEvent {

}

sealed interface ErrorUiEffect : UiEffect {

}