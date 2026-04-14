package site.dogether.presentation.screen.participation_method

import androidx.lifecycle.SavedStateHandle
import site.dogether.KEY_IS_PARTICIPATING_GROUP_EXIST
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

class ParticipationMethodViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel<ParticipationMethodUiState>(ParticipationMethodUiState()) {

    init {
        savedStateHandle.get<Boolean>(KEY_IS_PARTICIPATING_GROUP_EXIST)?.let { isParticipatingGroupExist ->
            updateState { it.copy(isParticipatingGroupExist = isParticipatingGroupExist) }
        }
    }

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