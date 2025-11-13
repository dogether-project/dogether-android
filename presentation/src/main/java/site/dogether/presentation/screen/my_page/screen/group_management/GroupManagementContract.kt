package site.dogether.presentation.screen.my_page.screen.group_management

import site.dogether.domain.model.group.Group
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.dialog_state.WithdrawGroupDialogState

data class GroupManagementUiState(
    val isLoading: Boolean = false,
    val groups: List<Group> = listOf(),
    val withdrawGroupDialogState: WithdrawGroupDialogState = WithdrawGroupDialogState(),
)

sealed interface GroupManagementUiEvent : UiEvent {
    sealed interface Click : GroupManagementUiEvent {
        data class OnClickWithdraw(val groupId: Int) : Click

        data object OnClickWithdrawDialogNegative : Click

        data object OnClickWithdrawDialogPositive : Click
    }

    sealed interface Callback : GroupManagementUiEvent {
        data object OnWithdrawDialogDismissRequested : Callback
    }
}

sealed interface GroupManagementUiEffect : UiEffect {
}