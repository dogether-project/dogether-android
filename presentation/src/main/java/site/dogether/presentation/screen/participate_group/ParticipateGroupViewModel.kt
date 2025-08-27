package site.dogether.presentation.screen.participate_group

import site.dogether.presentation.base.BaseViewModel

class ParticipateGroupViewModel : BaseViewModel<ParticipateGroupUiState, ParticipateGroupUiEvent, ParticipateGroupUiEffect>(ParticipateGroupUiState()) {

    override fun onEvent(event: ParticipateGroupUiEvent) {
        when (event) {
            is ParticipateGroupUiEvent.Type -> {
                when (event) {
                    is ParticipateGroupUiEvent.Type.OnJoinCodeTyped -> {
                        updateState { it.copy(joinCode = event.text) }
                    }
                }
            }
        }
    }
}