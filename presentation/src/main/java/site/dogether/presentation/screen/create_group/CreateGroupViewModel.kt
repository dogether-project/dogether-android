package site.dogether.presentation.screen.create_group

import site.dogether.presentation.base.BaseViewModel

class CreateGroupViewModel : BaseViewModel<CreateGroupUiState, CreateGroupUiEvent, CreateGroupUiEffect>(CreateGroupUiState()) {

    override fun onEvent(event: CreateGroupUiEvent) {
        when (event) {
            is CreateGroupUiEvent.Typed -> {
                when (event) {
                    is CreateGroupUiEvent.Typed.OnGroupNameTyped -> {
                        updateState { it.copy(name = event.text) }
                    }
                }
            }

            is CreateGroupUiEvent.Click -> {
                when (event) {
                    is CreateGroupUiEvent.Click.OnClickBack -> {
                        updateState(
                            condition = { it.currentPage != 0 },
                            reducer = { it.copy(currentPage = it.currentPage - 1) }
                        )
                    }

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

                    is CreateGroupUiEvent.Click.OnClickPeriod -> {
                        updateState { it.copy(period = event.period) }
                    }

                    is CreateGroupUiEvent.Click.OnClickLaunchFrom -> {
                        updateState { it.copy(isLaunchFromToday = event.isLaunchFromToday) }
                    }

                    is CreateGroupUiEvent.Click.OnClickCreateGroup -> {

                    }

                    is CreateGroupUiEvent.Click.OnClickDuplicatedNameDialogNegative -> {
                        updateState {
                            dismissDuplicatedNameDialog()
                            it.copy(
                                currentPage = 0
                            )
                        }
                    }

                    is CreateGroupUiEvent.Click.OnClickDuplicatedNameDialogPositive -> {
                        dismissDuplicatedNameDialog()
                    }
                }
            }

            is CreateGroupUiEvent.Callback -> {
                when (event) {
                    is CreateGroupUiEvent.Callback.OnDuplicatedNameDialogDismissRequested -> {
                        dismissDuplicatedNameDialog()
                    }
                }
            }
        }
    }

    private fun dismissDuplicatedNameDialog() {
        updateState { it.copy(duplicatedNameDialogState = it.duplicatedNameDialogState.copy(isShowing = false)) }
    }
}