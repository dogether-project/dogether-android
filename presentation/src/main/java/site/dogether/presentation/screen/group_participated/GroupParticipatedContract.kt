package site.dogether.presentation.screen.group_participated

import androidx.compose.runtime.Immutable
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
data class GroupParticipatedUiState(
    val isLoading: Boolean = false,
)

sealed interface GroupParticipatedUiEvent : UiEvent {

}

sealed interface GroupParticipatedUiEffect : UiEffect {

}