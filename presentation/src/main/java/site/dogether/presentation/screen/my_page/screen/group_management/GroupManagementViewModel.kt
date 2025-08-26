package site.dogether.presentation.screen.my_page.screen.group_management

import site.dogether.presentation.base.BaseViewModel

class GroupManagementViewModel : BaseViewModel<GroupManagementUiState, GroupManagementUiEvent, GroupManagementUiEffect>(GroupManagementUiState()) {

    override fun handleEvent(event: GroupManagementUiEvent) {
        when (event) {
            is GroupManagementUiEvent.Click -> {
                when (event) {
                    is GroupManagementUiEvent.Click.OnClickWithdraw -> {
                        updateState { it.copy(withdrawDialogState = it.withdrawDialogState.copy(isShowing = true)) }
                    }

                    is GroupManagementUiEvent.Click.OnClickWithdrawDialogNegative -> {
                        dismissWithdrawDialog()
                    }

                    is GroupManagementUiEvent.Click.OnClickWithdrawDialogPositive -> {
                        dismissWithdrawDialog()
                    }
                }
            }

            is GroupManagementUiEvent.Callback.OnWithdrawDialogDismissRequested -> {
                dismissWithdrawDialog()
            }
        }
    }

    private fun dismissWithdrawDialog() {
        updateState { it.copy(withdrawDialogState = it.withdrawDialogState.copy(isShowing = false)) }
    }
}