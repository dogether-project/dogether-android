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
                        if (uiState.currentPage != 0) {
                            updateState { it.copy(currentPage = it.currentPage - 1) }
                        } else {
                            postEffect(CreateGroupUiEffect.NavigateToBack)
                        }
                    }

                    is CreateGroupUiEvent.Click.OnClickMinusMemberLimit -> {
                        if (uiState.memberLimit > MinimumMemberLimit) {
                            updateState { it.copy(memberLimit = it.memberLimit - 1) }
                        }
                    }

                    is CreateGroupUiEvent.Click.OnClickPlusMemberLimit -> {
                        if (uiState.memberLimit < MaximumMemberLimit) {
                            updateState { it.copy(memberLimit = it.memberLimit + 1) }
                        }
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