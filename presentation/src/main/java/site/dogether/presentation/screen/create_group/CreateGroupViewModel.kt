package site.dogether.presentation.screen.create_group

import site.dogether.presentation.base.BaseViewModel

class CreateGroupViewModel : BaseViewModel<CreateGroupUiState, CreateGroupUiEvent, CreateGroupUiEffect>(CreateGroupUiState()) {

    override fun onEvent(event: CreateGroupUiEvent) {
        when (event) {
            is CreateGroupUiEvent.Type -> {
                when (event) {
                    is CreateGroupUiEvent.Type.OnGroupNameTyped -> {
                        updateState { it.copy(groupName = event.text) }
                    }
                }
            }

            is CreateGroupUiEvent.Click -> {
                when (event) {
                    is CreateGroupUiEvent.Click.OnClickMinusMemberLimit -> {
                        updateState(
                            condition = { it.memberLimit > MinimumMemberLimit },
                            reducer = { it.copy(memberLimit = it.memberLimit - 1) }
                        )
                    }

                    is CreateGroupUiEvent.Click.OnClickPlusMemberLimit -> {
                        updateState(
                            condition = { it.memberLimit < MaximumMemberLimit },
                            reducer = { it.copy(memberLimit = it.memberLimit + 1) }
                        )
                    }

                    is CreateGroupUiEvent.Click.OnClickNext -> {
                        updateState { it.copy(currentPage = it.currentPage + 1) }
                    }
                }
            }
        }
    }
}