package site.dogether.presentation.screen.participate_group

import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class ParticipateGroupViewModel : BaseViewModel<ParticipateGroupUiState>(ParticipateGroupUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

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