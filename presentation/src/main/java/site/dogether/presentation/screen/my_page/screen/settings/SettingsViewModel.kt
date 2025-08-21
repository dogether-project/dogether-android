package site.dogether.presentation.screen.my_page.screen.settings

import site.dogether.presentation.base.BaseViewModel

class SettingsViewModel : BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsUiEffect>(SettingsUiState()) {

    override fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.Click -> {
                when (event) {
                    is SettingsUiEvent.Click.OnClickLogout -> {
                        updateState { it.copy(logoutDialogState = it.logoutDialogState.copy(isShowing = true)) }
                    }

                    is SettingsUiEvent.Click.OnClickLogoutDialogNegative -> {
                        dismissLogoutDialog()
                    }

                    is SettingsUiEvent.Click.OnClickLogoutDialogPositive -> {
                        dismissLogoutDialog()
                    }

                    is SettingsUiEvent.Click.OnClickWithdraw -> {
                        updateState { it.copy(withdrawDialogState = it.withdrawDialogState.copy(isShowing = true)) }
                    }

                    is SettingsUiEvent.Click.OnClickWithdrawDialogNegative -> {
                        dismissWithdrawDialog()
                    }

                    is SettingsUiEvent.Click.OnClickWithdrawDialogPositive -> {
                        dismissWithdrawDialog()
                    }
                }
            }

            is SettingsUiEvent.Callback -> {
                when (event) {
                    is SettingsUiEvent.Callback.OnLogoutDialogDismissRequested -> {
                        dismissLogoutDialog()
                    }

                    is SettingsUiEvent.Callback.OnWithdrawDialogDismissRequested -> {
                        dismissWithdrawDialog()
                    }
                }
            }
        }
    }

    private fun dismissLogoutDialog() {
        updateState { it.copy(logoutDialogState = it.logoutDialogState.copy(isShowing = false)) }
    }

    private fun dismissWithdrawDialog() {
        updateState { it.copy(withdrawDialogState = it.withdrawDialogState.copy(isShowing = false)) }
    }
}