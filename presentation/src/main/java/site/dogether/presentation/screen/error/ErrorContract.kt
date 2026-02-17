package site.dogether.presentation.screen.error

import androidx.compose.runtime.Immutable
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.screen.error.model.Error

@Immutable
data class ErrorUiState(
    val error: Error? = Error.Unknown,
)

sealed interface ErrorUiEvent : UiEvent {
    data object OnClickPositive : ErrorUiEvent
    data object OnClickNegative : ErrorUiEvent
}

sealed interface ErrorUiEffect : UiEffect {
    data object ExecutePositiveCallback : ErrorUiEffect
    data object ExecuteNegativeCallback : ErrorUiEffect
}