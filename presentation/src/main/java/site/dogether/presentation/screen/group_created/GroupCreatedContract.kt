package site.dogether.presentation.screen.group_created

import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

data class GroupCreatedUiState(
    val isLoading: Boolean = false,
    val joinCode: String = "",
)

sealed interface GroupCreatedUiEvent : UiEvent {
    sealed interface Click : GroupCreatedUiEvent {
        data object OnClickShare : Click

        data object OnClickNavigateToHome : Click
    }
}

sealed interface GroupCreatedUiEffect : UiEffect {
    data class ShareJoinCode(val joinCode: String) : GroupCreatedUiEffect

    data object NavigateToHome : GroupCreatedUiEffect
}