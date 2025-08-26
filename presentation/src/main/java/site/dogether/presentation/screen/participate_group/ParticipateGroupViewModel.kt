package site.dogether.presentation.screen.participate_group

import site.dogether.presentation.base.BaseViewModel

class ParticipateGroupViewModel : BaseViewModel<ParticipateGroupUiState, ParticipateGroupUiEvent, ParticipateGroupUiEffect>(ParticipateGroupUiState()) {

    override fun handleEvent(event: ParticipateGroupUiEvent) {
        when (event) {
            is ParticipateGroupUiEvent.Type -> {
                when (event) {
                    is ParticipateGroupUiEvent.Type.OnInviteCodeTyped -> {
                        updateState { it.copy(inviteCode = event.text) }
                    }
                }
            }
        }
    }
}