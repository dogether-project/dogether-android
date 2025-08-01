package site.dogether.presentation.screen.participation_method

import site.dogether.presentation.base.BaseViewModel

class ParticipationMethodViewModel : BaseViewModel<ParticipationMethodUiState, ParticipationMethodUiEvent, ParticipationMethodUiEffect>(ParticipationMethodUiState()) {

    override fun onEvent(event: ParticipationMethodUiEvent) {
        when (event) {
            is ParticipationMethodUiEvent.Lifecycle -> {
                when (event) {
                    is ParticipationMethodUiEvent.Lifecycle.OnFirstComposition -> {
                        postEffect(ParticipationMethodUiEffect.CheckNotificationPermission)
                    }
                }
            }

            is ParticipationMethodUiEvent.Click -> {
                when (event) {
                    is ParticipationMethodUiEvent.Click.OnClickCreateGroup -> {
                        postEffect(ParticipationMethodUiEffect.NavigateToCreateGroup)
                    }

                    is ParticipationMethodUiEvent.Click.OnClickParticipateWithCode -> {
                        postEffect(ParticipationMethodUiEffect.NavigateToParticipateWithCode)
                    }

                    is ParticipationMethodUiEvent.Click.OnClickPermissionDialogNegative -> {
                        dismissPermissionDialog()
                    }

                    is ParticipationMethodUiEvent.Click.OnClickPermissionDialogPositive -> {
                        dismissPermissionDialog()
                        postEffect(ParticipationMethodUiEffect.NavigateToNotificationSetting)
                    }
                }
            }

            is ParticipationMethodUiEvent.Callback -> {
                when (event) {
                    is ParticipationMethodUiEvent.Callback.OnPermissionDenied -> {
                        updateState { it.copy(permissionDialogState = it.permissionDialogState.copy(isShowing = true)) }
                    }

                    is ParticipationMethodUiEvent.Callback.OnPermissionDialogDismissRequested -> {
                        dismissPermissionDialog()
                    }
                }
            }
        }
    }

    private fun dismissPermissionDialog() {
        updateState { it.copy(permissionDialogState = it.permissionDialogState.copy(isShowing = false)) }
    }
}