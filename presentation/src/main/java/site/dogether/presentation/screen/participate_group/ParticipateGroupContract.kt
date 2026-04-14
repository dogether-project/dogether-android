package site.dogether.presentation.screen.participate_group

import androidx.compose.runtime.Immutable
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent
import site.dogether.presentation.model.dialog_state.ActionDialogState

@Immutable
data class ParticipateGroupUiState(
    val isLoading: Boolean = false,
    val joinCode: String = "",
    val actionDialogState: ActionDialogState = ActionDialogState()
) {
    val isValid: Boolean
        get() = joinCode.length >= 8
}

sealed interface ParticipateGroupUiEvent : UiEvent {
    sealed interface Type : ParticipateGroupUiEvent {
        data class OnJoinCodeTyped(val text: String) : Type
    }

    sealed interface Click : ParticipateGroupUiEvent {
        data object OnClickParticipate : Click

        data object OnClickActionDialogNegative : Click

        data object OnClickActionDialogPositive : Click
    }
}

sealed interface ParticipateGroupUiEffect : UiEffect {

}