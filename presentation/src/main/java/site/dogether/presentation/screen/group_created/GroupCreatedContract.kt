package site.dogether.presentation.screen.group_created

data class GroupCreatedUiState(
    val isLoading: Boolean = false,
    val joinCode: String = ""
)

sealed interface GroupCreatedUiEvent {

}

sealed interface GroupCreatedUiEffect {

}