package site.dogether.presentation.screen.participate_group

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.group.ParticipateGroupUseCase
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

class ParticipateGroupViewModel(private val participateGroup: ParticipateGroupUseCase) : BaseViewModel<ParticipateGroupUiState>(ParticipateGroupUiState()) {

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

            is ParticipateGroupUiEvent.Click -> {
                when (event) {
                    is ParticipateGroupUiEvent.Click.OnClickParticipate -> {
                        viewModelScope.launch {
                            val participateGroupResult = participateGroup(uiState.joinCode).getOrElse { e ->
                                // handle exception
                                return@launch
                            }

                            postEffect(UiEffect.NavigateTo(Screen.HOME))
                        }
                    }
                }
            }
        }
    }
}