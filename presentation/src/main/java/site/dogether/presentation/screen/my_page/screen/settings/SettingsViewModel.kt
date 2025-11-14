package site.dogether.presentation.screen.my_page.screen.settings

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.user.LogoutUseCase
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

class SettingsViewModel(
    private val logout: LogoutUseCase
) : BaseViewModel<SettingsUiState>(SettingsUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

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
                        viewModelScope.launch {
                            logout()
                            postEffect(
                                UiEffect.NavigateTo(
                                    screen = Screen.ON_BOARDING,
                                    clearBackStack = true
                                )
                            )
                        }
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