package site.dogether.presentation.screen.my_page.screen.group_management

import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.DialogState

data class GroupManagementUiState(
    val isLoading: Boolean = false,
    val withdrawDialogState: DialogState = DialogState(),
)

sealed interface GroupManagementUiEvent : UiEvent {
    sealed interface Click : GroupManagementUiEvent {
        data class OnClickWithdraw(val group: String) : Click

        data object OnClickWithdrawDialogNegative : Click

        data object OnClickWithdrawDialogPositive : Click
    }

    sealed interface Callback : GroupManagementUiEvent {
        data object OnWithdrawDialogDismissRequested : Callback
    }
}

sealed interface GroupManagementUiEffect : UiEffect {

}