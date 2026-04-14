package site.dogether.presentation.screen.group_created

import androidx.lifecycle.SavedStateHandle
import site.dogether.KEY_GROUP_NAME
import site.dogether.KEY_JOIN_CODE
import site.dogether.presentation.Screen
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEffect
import site.dogether.presentation.base.UiEvent

class GroupCreatedViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel<GroupCreatedUiState>(GroupCreatedUiState()) {
    
    val groupName = savedStateHandle.get<String>(KEY_GROUP_NAME).orEmpty()

    init {
        savedStateHandle.get<String>(KEY_JOIN_CODE)?.let { joinCode ->
            updateState { it.copy(joinCode = joinCode) }
        }
    }

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

        when (event) {
            is GroupCreatedUiEvent.Click -> {
                when (event) {
                    is GroupCreatedUiEvent.Click.OnClickShare -> {
                        postEffect(
                            GroupCreatedUiEffect.ShareJoinCode(
                                groupName = groupName,
                                joinCode = uiState.joinCode
                            )
                        )
                    }

                    is GroupCreatedUiEvent.Click.OnClickNavigateToHome -> {
                        postEffect(
                            UiEffect.NavigateTo(
                                screen = Screen.HOME,
                                clearBackStack = true
                            )
                        )
                    }
                }
            }
        }
    }
}