package site.dogether.presentation.screen.my_page.screen.group_management

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class GroupManagementViewModel : BaseViewModel<GroupManagementUiState>(GroupManagementUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

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