package site.dogether.presentation.screen.participation_method

import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

class ParticipationMethodViewModel : BaseViewModel<ParticipationMethodUiState>(ParticipationMethodUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is ParticipationMethodUiEvent.Click -> {
                when (event) {
                    is ParticipationMethodUiEvent.Click.OnClickCreateGroup -> {
                        postEffect(ParticipationMethodUiEffect.NavigateToCreateGroup)
                    }

                    is ParticipationMethodUiEvent.Click.OnClickParticipateGroup -> {
                        postEffect(ParticipationMethodUiEffect.NavigateToParticipateGroup)
                    }

                    is ParticipationMethodUiEvent.Click.OnClickMyPage -> {
                        postEffect(UiEffect.NavigateTo(Screen.MY_PAGE))
                    }
                }
            }
        }
    }
}