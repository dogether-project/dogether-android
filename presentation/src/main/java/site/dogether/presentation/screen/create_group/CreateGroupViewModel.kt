package site.dogether.presentation.screen.create_group

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.dogether.domain.use_case.group.CreateGroupUseCase
import site.dogether.presentation.base.BaseViewModel
import site.dogether.presentation.base.UiEvent

class CreateGroupViewModel(private val createGroup: CreateGroupUseCase) : BaseViewModel<CreateGroupUiState>(CreateGroupUiState()) {

    override fun onEvent(event: UiEvent) {
        super.onEvent(event)

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

                    is CreateGroupUiEvent.Click.OnClickReduceMaximumMemberCount -> {
                        if (uiState.maximumMemberCount > MinimumMemberCount) {
                            updateState { it.copy(maximumMemberCount = it.maximumMemberCount - 1) }
                        }
                    }

                    is CreateGroupUiEvent.Click.OnClickAddMaximumMemberCount -> {
                        if (uiState.maximumMemberCount < MaximumMemberCount) {
                            updateState { it.copy(maximumMemberCount = it.maximumMemberCount + 1) }
                        }
                    }

                    is CreateGroupUiEvent.Click.OnClickNext -> {
                        updateState { it.copy(currentPage = it.currentPage + 1) }
                    }

                    is CreateGroupUiEvent.Click.OnClickDuration -> {
                        updateState { it.copy(duration = event.duration) }
                    }

                    is CreateGroupUiEvent.Click.OnClickLaunchFrom -> {
                        updateState { it.copy(isLaunchFromToday = event.isLaunchFromToday) }
                    }

                    is CreateGroupUiEvent.Click.OnClickCreateGroup -> {
                        viewModelScope.launch {
                            val createGroupResult = createGroup(
                                name = uiState.name,
                                maximumMemberCount = uiState.maximumMemberCount,
                                isLaunchFromToday = uiState.isLaunchFromToday,
                                duration = uiState.duration
                            ).getOrElse { throwable ->
                                return@launch
                            }

                            postEffect(CreateGroupUiEffect.NavigateToGroupCreated(createGroupResult.joinCode))
                        }
                    }

                    is CreateGroupUiEvent.Click.OnClickDuplicatedNameDialogNegative -> {
                        updateState {
                            dismissDuplicatedNameDialog()
                            it.copy(currentPage = 0)
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