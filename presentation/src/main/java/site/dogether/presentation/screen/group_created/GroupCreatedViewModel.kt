package site.dogether.presentation.screen.group_created

import androidx.lifecycle.SavedStateHandle
import site.dogether.KEY_JOIN_CODE
import site.dogether.presentation.base.BaseViewModel

class GroupCreatedViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel<GroupCreatedUiState, GroupCreatedUiEvent, GroupCreatedUiEffect>(GroupCreatedUiState()) {

    init {
        savedStateHandle.get<String>(KEY_JOIN_CODE)?.let { joinCode ->
            updateState { it.copy(joinCode = joinCode) }
        }
    }

    override fun onEvent(event: GroupCreatedUiEvent) {
        when (event) {
            is GroupCreatedUiEvent.Click -> {
                when(event) {
                    is GroupCreatedUiEvent.Click.OnClickShare -> {
                        postEffect(GroupCreatedUiEffect.ShareJoinCode(uiState.joinCode))
                    }

                    is GroupCreatedUiEvent.Click.OnClickNavigateToHome -> {
                        postEffect(GroupCreatedUiEffect.NavigateToHome)
                    }
                }
            }
        }
    }
}