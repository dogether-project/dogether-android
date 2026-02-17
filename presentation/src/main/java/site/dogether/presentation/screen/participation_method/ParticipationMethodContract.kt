package site.dogether.presentation.screen.participation_method

import androidx.compose.runtime.Immutable
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
data class ParticipationMethodUiState(
    val isLoading: Boolean = false,
    val isParticipatingGroupExist: Boolean = false,
)

sealed interface ParticipationMethodUiEvent : UiEvent {
    sealed interface Click : ParticipationMethodUiEvent {
        data object OnClickCreateGroup : Click

        data object OnClickParticipateGroup : Click

        data object OnClickMyPage : Click
    }
}

sealed interface ParticipationMethodUiEffect : UiEffect {
    data object NavigateToCreateGroup : ParticipationMethodUiEffect

    data object NavigateToParticipateGroup : ParticipationMethodUiEffect
}