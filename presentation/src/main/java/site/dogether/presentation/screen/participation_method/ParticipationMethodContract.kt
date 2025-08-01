package site.dogether.presentation.screen.participation_method

import site.dogether.presentation.model.DialogState

data class ParticipationMethodUiState(
    val isLoading: Boolean = false,
    val permissionDialogState: DialogState = DialogState(),
    val isParticipatingGroupExist: Boolean = false
)

sealed interface ParticipationMethodUiEvent {
    sealed interface Lifecycle : ParticipationMethodUiEvent {
        data object OnFirstComposition : Lifecycle
    }

    sealed interface Click : ParticipationMethodUiEvent {
        data object OnClickCreateGroup : Click

        data object OnClickParticipateWithCode : Click

        data object OnClickPermissionDialogNegative : Click

        data object OnClickPermissionDialogPositive : Click
    }

    sealed interface Callback : ParticipationMethodUiEvent {
        data object OnPermissionDenied : Callback

        data object OnPermissionDialogDismissRequested : Callback
    }
}

sealed interface ParticipationMethodUiEffect {
    data object NavigateToCreateGroup : ParticipationMethodUiEffect

    data object NavigateToParticipateWithCode : ParticipationMethodUiEffect

    data object CheckNotificationPermission : ParticipationMethodUiEffect

    data object NavigateToNotificationSetting : ParticipationMethodUiEffect
}