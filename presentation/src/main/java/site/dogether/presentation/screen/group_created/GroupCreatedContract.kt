package site.dogether.presentation.screen.group_created

import androidx.compose.runtime.Immutable
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

@Immutable
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
    data class ShareJoinCode(
        val groupName: String,
        val joinCode: String
    ) : GroupCreatedUiEffect
}