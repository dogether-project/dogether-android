package site.dogether.presentation.screen.group_participated

data class GroupParticipatedUiState(
    val isLoading: Boolean = false,
)

sealed interface GroupParticipatedUiEvent {

}

sealed interface GroupParticipatedUiEffect {

}