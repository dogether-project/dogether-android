package site.dogether.presentation.screen.my_page.screen.settings

import androidx.compose.runtime.Immutable
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.dialog_state.DialogState

@Immutable
data class SettingsUiState(
    val isLoading: Boolean = false,
    val logoutDialogState: DialogState = DialogState(),
    val withdrawDialogState: DialogState = DialogState(),
)

sealed interface SettingsUiEvent : UiEvent {
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

sealed interface SettingsUiEffect : UiEffect {
    data object WithdrawWithKakao : SettingsUiEffect

    data object NavigateToOnBoarding : SettingsUiEffect
}