package site.dogether.presentation.screen.my_page.screen.settings

import site.dogether.presentation.model.DialogState

data class SettingsUiState(
    val logoutDialogState: DialogState = DialogState(),
    val withdrawDialogState: DialogState = DialogState(),
)

sealed interface SettingsUiEvent {
    sealed interface Click : SettingsUiEvent {
        data object OnClickLogout : Click

        data object OnClickLogoutDialogNegative : Click

        data object OnClickLogoutDialogPositive : Click

        data object OnClickWithdraw : Click

        data object OnClickWithdrawDialogNegative : Click

        data object OnClickWithdrawDialogPositive : Click
    }

    sealed interface Callback : SettingsUiEvent {
        data object OnLogoutDialogDismissRequested : Callback

        data object OnWithdrawDialogDismissRequested : Callback
    }
}

sealed interface SettingsUiEffect {

}