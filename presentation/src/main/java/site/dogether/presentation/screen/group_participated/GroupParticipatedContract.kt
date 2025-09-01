package site.dogether.presentation.screen.group_participated

import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class GroupParticipatedUiState(
    val isLoading: Boolean = false,
)

sealed interface GroupParticipatedUiEvent : UiEvent {

}

sealed interface GroupParticipatedUiEffect : UiEffect {

}