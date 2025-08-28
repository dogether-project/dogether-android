package site.dogether.presentation.screen.participate_group

import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class ParticipateGroupUiState(
    val isLoading: Boolean = false,
    val joinCode: String = "",
) {
    val isValid: Boolean
        get() = joinCode.length >= 8
}

sealed interface ParticipateGroupUiEvent : UiEvent {
    sealed interface Type : ParticipateGroupUiEvent {
        data class OnJoinCodeTyped(val text: String) : Type
    }
}

sealed interface ParticipateGroupUiEffect : UiEffect {

}