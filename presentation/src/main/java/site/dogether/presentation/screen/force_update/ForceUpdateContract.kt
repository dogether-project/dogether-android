package site.dogether.presentation.screen.force_update

import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class ForceUpdateUiState(
    val isLoading: Boolean = false,
)

sealed interface ForceUpdateUiEvent : UiEvent {
    sealed interface Click : ForceUpdateUiEvent {
        data object OnClickUpdate : Click
    }
}

sealed interface ForceUpdateUiEffect : UiEffect {

}