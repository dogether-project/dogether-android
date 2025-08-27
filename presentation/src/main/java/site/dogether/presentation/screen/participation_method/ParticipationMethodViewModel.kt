package site.dogether.presentation.screen.participation_method

import site.dogether.presentation.base.BaseViewModel

class ParticipationMethodViewModel : BaseViewModel<ParticipationMethodUiState, ParticipationMethodUiEvent, ParticipationMethodUiEffect>(ParticipationMethodUiState()) {

    override fun onEvent(event: ParticipationMethodUiEvent) {
        when (event) {
            is ParticipationMethodUiEvent.Click -> {
                when (event) {
                    is ParticipationMethodUiEvent.Click.OnClickCreateGroup -> {
                        postEffect(ParticipationMethodUiEffect.NavigateToCreateGroup)
                    }

                    is ParticipationMethodUiEvent.Click.OnClickParticipateGroup -> {
                        postEffect(ParticipationMethodUiEffect.NavigateToParticipateGroup)
                    }
                }
            }
        }
    }
}