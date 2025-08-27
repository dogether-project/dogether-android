package site.dogether.presentation.screen.participation_method

data class ParticipationMethodUiState(
    val isLoading: Boolean = false,
    val isParticipatingGroupExist: Boolean = false,
)

sealed interface ParticipationMethodUiEvent {
    sealed interface Click : ParticipationMethodUiEvent {
        data object OnClickCreateGroup : Click

        data object OnClickParticipateGroup : Click
    }
}

sealed interface ParticipationMethodUiEffect {
    data object NavigateToCreateGroup : ParticipationMethodUiEffect

    data object NavigateToParticipateGroup : ParticipationMethodUiEffect
}