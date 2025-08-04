package site.dogether.presentation.screen.group_created

data class GroupCreatedUiState(
    val isLoading: Boolean = false,
    val code: String = "011210"
)

sealed interface GroupCreatedUiEvent {

}

sealed interface GroupCreatedUiEffect {

}