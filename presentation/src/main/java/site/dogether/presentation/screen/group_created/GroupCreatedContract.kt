package site.dogether.presentation.screen.group_created

data class GroupCreatedUiState(
    val isLoading: Boolean = false,
    val joinCode: String = "",
)

sealed interface GroupCreatedUiEvent {
    sealed interface Click : GroupCreatedUiEvent {
        data object OnClickShare : Click

        data object OnClickNavigateToHome : Click
    }
}

sealed interface GroupCreatedUiEffect {
    data class ShareJoinCode(val joinCode: String) : GroupCreatedUiEffect

    data object NavigateToHome : GroupCreatedUiEffect
}